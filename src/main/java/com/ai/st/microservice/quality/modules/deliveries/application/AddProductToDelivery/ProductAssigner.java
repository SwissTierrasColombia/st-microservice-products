package com.ai.st.microservice.quality.modules.deliveries.application.AddProductToDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.FindProductsFromDelivery.DeliveryProductsFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.FindProductsFromDelivery.FindProductsFromDeliveryQuery;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.DeliverySearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.SearchDeliveryQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.OperatorDoesNotBelongToDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.ProductHasBeenAlreadyAddedToDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.*;

import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.application.FindProductsFromManager.ManagerProductsFinder;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDoesNotBelongToManager;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;

@Service
public final class ProductAssigner {

    private final DeliveryProductRepository repository;
    private final DeliverySearcher deliverySearcher;
    private final ManagerProductsFinder productsFinderByManager;
    private final DeliveryProductsFinder deliveryProductsFinder;
    private final DateTime dateTime;

    public ProductAssigner(DeliveryProductRepository repository, DeliveryRepository deliveryRepository,
                           ProductRepository productRepository, DeliveryProductRepository deliveryProductRepository,
                           DateTime dateTime) {
        this.repository = repository;
        this.deliverySearcher = new DeliverySearcher(deliveryRepository);
        this.productsFinderByManager = new ManagerProductsFinder(productRepository);
        this.dateTime = dateTime;
        this.deliveryProductsFinder = new DeliveryProductsFinder(deliveryProductRepository, deliveryRepository);
    }

    public void assign(AddProductToDeliveryCommand command) {

        DeliveryId deliveryId = new DeliveryId(command.deliveryId());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());
        ProductId productId = new ProductId(command.productId());

        DeliveryResponse deliveryResponse = verifyDeliveryExists(deliveryId, operatorCode);
        verifyOperatorIsOwnerDelivery(deliveryResponse, operatorCode);
        verifyProductBelongToManager(productId, new ManagerCode(deliveryResponse.managerCode()));
        verifyIfProductHasBeenAlreadyAddedToDelivery(deliveryId, productId, operatorCode);

        DeliveryProduct deliveryProduct = DeliveryProduct.create(
                new DeliveryProductDate(dateTime.now()),
                new DeliveryProductObservations(""), productId,
                new DeliveryProductStatusId(DeliveryProductStatusId.PENDING));

        repository.save(deliveryId, deliveryProduct);
    }

    private DeliveryResponse verifyDeliveryExists(DeliveryId deliveryId, OperatorCode operatorCode) {
        return deliverySearcher.search(new SearchDeliveryQuery(
                deliveryId.value(), Roles.OPERATOR, operatorCode.value()
        ));
    }

    private void verifyOperatorIsOwnerDelivery(DeliveryResponse deliveryResponse, OperatorCode operatorCode) {
        if (!deliveryResponse.operatorCode().equals(operatorCode.value())) {
            throw new OperatorDoesNotBelongToDelivery();
        }
    }

    private void verifyProductBelongToManager(ProductId productId, ManagerCode managerCode) {
        List<ProductResponse> productResponseList = this.productsFinderByManager.finder(managerCode.value());
        productResponseList.stream().filter(productResponse -> productResponse.id().equals(productId.value())).findAny()
                .orElseThrow(ProductDoesNotBelongToManager::new);
    }

    private void verifyIfProductHasBeenAlreadyAddedToDelivery(DeliveryId deliveryId, ProductId productId, OperatorCode operatorCode) {

        List<DeliveryProductResponse> deliveryProducts = deliveryProductsFinder.finder(new FindProductsFromDeliveryQuery(
                deliveryId.value(), Roles.OPERATOR, operatorCode.value()
        ));

        deliveryProducts.stream().filter(deliveryProductResponse -> deliveryProductResponse.productId().equals(productId.value()))
                .findAny().ifPresent(s -> {
            throw new ProductHasBeenAlreadyAddedToDelivery();
        });

    }


}
