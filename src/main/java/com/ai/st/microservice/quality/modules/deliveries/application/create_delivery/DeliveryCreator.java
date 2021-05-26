package com.ai.st.microservice.quality.modules.deliveries.application.create_delivery;

import com.ai.st.microservice.quality.modules.deliveries.domain.*;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductDate;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductObservations;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatusId;

import com.ai.st.microservice.quality.modules.products.application.find_products_from_manager.ManagerProductsFinderQuery;
import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.application.find_products_from_manager.ManagerProductsFinder;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDoesNotBelongToManager;

import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.*;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.OperatorMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.WorkspaceMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.OperatorDoesNotBelongToManager;
import org.apache.commons.lang.RandomStringUtils;

import java.util.List;

@Service
public final class DeliveryCreator implements CommandUseCase<CreateDeliveryCommand> {

    private final DeliveryRepository deliveryRepository;
    private final WorkspaceMicroservice workspaceMicroservice;
    private final ManagerMicroservice managerMicroservice;
    private final OperatorMicroservice operatorMicroservice;
    private final DateTime dateTime;
    private final ManagerProductsFinder managerProductsFinder;

    public DeliveryCreator(DeliveryRepository deliveryRepository, ProductRepository productRepository,
                           WorkspaceMicroservice workspaceMicroservice, ManagerMicroservice managerMicroservice,
                           OperatorMicroservice operatorMicroservice, DateTime dateTime) {
        this.deliveryRepository = deliveryRepository;
        this.workspaceMicroservice = workspaceMicroservice;
        this.managerMicroservice = managerMicroservice;
        this.operatorMicroservice = operatorMicroservice;
        this.dateTime = dateTime;
        this.managerProductsFinder = new ManagerProductsFinder(productRepository);
    }

    @Override
    public void handle(CreateDeliveryCommand command) {

        MunicipalityCode municipalityCode = new MunicipalityCode(command.municipalityCode());
        ManagerCode managerCode = new ManagerCode(command.managerCode());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());

        verifyOperatorBelongToManager(operatorCode, managerCode, municipalityCode);
        verifyProductsBelongToManager(command.deliveryProducts(), managerCode);

        DepartmentMunicipality departmentMunicipality = findDepartmentMunicipality(municipalityCode);

        Delivery delivery = Delivery.create(
                new DeliveryCode(generateDeliveryCode()),
                municipalityCode,
                departmentMunicipality.municipality(),
                departmentMunicipality.department(),
                managerCode,
                findManagerName(managerCode),
                operatorCode,
                findOperatorName(operatorCode),
                new UserCode(command.userCode()),
                new DeliveryObservations(command.observations()),
                new DeliveryDate(dateTime.now()),
                new DeliveryStatusId(DeliveryStatusId.DRAFT)
        );

        command.deliveryProducts().forEach(productId -> delivery.addProduct(
                new DeliveryProductDate(dateTime.now()),
                new DeliveryProductObservations(""),
                new ProductId(productId),
                new DeliveryProductStatusId(DeliveryProductStatusId.PENDING)
        ));

        deliveryRepository.save(delivery);
    }

    private void verifyOperatorBelongToManager(OperatorCode operatorCode, ManagerCode managerCode, MunicipalityCode municipalityCode) {
        boolean belong = workspaceMicroservice.verifyOperatorBelongToManager(operatorCode, managerCode, municipalityCode);
        if (!belong) {
            throw new OperatorDoesNotBelongToManager();
        }
    }

    private void verifyProductsBelongToManager(List<Long> productsId, ManagerCode managerCode) {
        productsId.forEach(productId -> this.verifyProductBelongToManager(new ProductId(productId), managerCode));
    }

    private void verifyProductBelongToManager(ProductId productId, ManagerCode managerCode) {
        List<ProductResponse> productResponseList = this.managerProductsFinder.
                handle(new ManagerProductsFinderQuery(managerCode.value())).list();
        productResponseList.stream().filter(productResponse -> productResponse.id().equals(productId.value())).findAny()
                .orElseThrow(ProductDoesNotBelongToManager::new);
    }

    private String generateDeliveryCode() {
        return RandomStringUtils.random(6, false, true);
    }

    private ManagerName findManagerName(ManagerCode managerCode) {
        return managerMicroservice.getManagerName(managerCode);
    }

    private OperatorName findOperatorName(OperatorCode operatorCode) {
        return operatorMicroservice.getOperatorName(operatorCode);
    }

    private DepartmentMunicipality findDepartmentMunicipality(MunicipalityCode municipalityCode) {
        return workspaceMicroservice.getDepartmentMunicipalityName(municipalityCode);
    }

}
