package com.ai.st.microservice.quality.modules.deliveries.application.AddProductToDelivery;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.*;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.*;

import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.application.FindProductsFromManager.ManagerProductsFinder;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDoesNotBelongToManager;

import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;

@Service
public final class DeliveryProductAssigner {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final ManagerProductsFinder managerProductsFinder;
    private final DateTime dateTime;

    public DeliveryProductAssigner(DeliveryProductRepository deliveryProductRepository, DeliveryRepository deliveryRepository,
                                   ProductRepository productRepository, DateTime dateTime) {
        this.dateTime = dateTime;
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.managerProductsFinder = new ManagerProductsFinder(productRepository);
    }

    public void assign(DeliveryProductAssignerCommand command) {

        DeliveryId deliveryId = new DeliveryId(command.deliveryId());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());
        ProductId productId = new ProductId(command.productId());

        verifyPermissions(deliveryId, productId, operatorCode);

        DeliveryProduct deliveryProduct = DeliveryProduct.create(
                new DeliveryProductDate(dateTime.now()),
                new DeliveryProductObservations(""), productId,
                new DeliveryProductStatusId(DeliveryProductStatusId.PENDING));

        deliveryProductRepository.save(deliveryId, deliveryProduct);
    }

    private void verifyPermissions(DeliveryId deliveryId, ProductId productId, OperatorCode operatorCode) {


        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToOperator(operatorCode)) {
            throw new OperatorDoesNotBelongToDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery("No se puede agregar el producto, porque el estado de la entrega no lo permite.");
        }

        verifyProductBelongToManager(productId.value(), delivery.manager().value());

        verifyIfProductHasBeenAlreadyAddedToDelivery(deliveryId, productId);
    }

    private void verifyProductBelongToManager(Long productId, Long managerCode) {
        List<ProductResponse> productResponseList = this.managerProductsFinder.finder(managerCode);
        productResponseList.stream().filter(productResponse -> productResponse.id().equals(productId)).findAny()
                .orElseThrow(ProductDoesNotBelongToManager::new);
    }

    private void verifyIfProductHasBeenAlreadyAddedToDelivery(DeliveryId deliveryId, ProductId productId) {
        List<DeliveryProduct> deliveryProducts = deliveryProductRepository.findProductsFromDelivery(deliveryId);
        deliveryProducts.stream().filter(deliveryProduct -> deliveryProduct.productId().value().equals(productId.value()))
                .findAny().ifPresent(s -> {
            throw new ProductHasBeenAlreadyAddedToDelivery();
        });
    }

}
