package com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryDate;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryObservations;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductDate;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductObservations;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusId;

import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.application.SearchProductsFromManager.ProductsFinderByManager;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDoesNotBelongToManager;

import com.ai.st.microservice.quality.modules.shared.domain.*;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.WorkspaceMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.OperatorDoesNotBelongToManager;

import java.util.List;

@Service
public final class DeliveryCreator {

    private final DeliveryRepository deliveryRepository;
    private final WorkspaceMicroservice workspaceMicroservice;
    private final DateTime dateTime;
    private final ProductsFinderByManager productsFinderByManager;

    public DeliveryCreator(DeliveryRepository deliveryRepository, ProductRepository productRepository,
                           WorkspaceMicroservice workspaceMicroservice, DateTime dateTime) {
        this.deliveryRepository = deliveryRepository;
        this.workspaceMicroservice = workspaceMicroservice;
        this.dateTime = dateTime;
        this.productsFinderByManager = new ProductsFinderByManager(productRepository);
    }

    public void create(CreateDeliveryCommand command) {

        MunicipalityCode municipalityCode = new MunicipalityCode(command.municipalityCode());
        ManagerCode managerCode = new ManagerCode(command.managerCode());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());

        verifyOperatorBelongToManager(operatorCode, managerCode, municipalityCode);
        verifyProductsBelongToManager(command.deliveryProducts(), managerCode);

        Delivery delivery = Delivery.create(
                municipalityCode,
                managerCode,
                operatorCode,
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
        List<ProductResponse> productResponseList = this.productsFinderByManager.finder(managerCode.value());
        productResponseList.stream().filter(productResponse -> productResponse.id().equals(productId.value())).findAny()
                .orElseThrow(ProductDoesNotBelongToManager::new);
    }

}
