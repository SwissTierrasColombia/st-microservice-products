package com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.DeliverySearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.DeliverySearcherQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryProductSearcher {

    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliverySearcher deliverySearcher;

    public DeliveryProductSearcher(DeliveryProductRepository deliveryProductRepository, DeliveryRepository deliveryRepository) {
        this.deliveryProductRepository = deliveryProductRepository;
        this.deliverySearcher = new DeliverySearcher(deliveryRepository);
    }

    public DeliveryProductResponse search(DeliveryProductSearcherQuery query) {

        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());

        DeliveryProduct deliveryProduct = deliveryProductRepository.search(deliveryProductId);

        verifyDeliveryProduct(deliveryProduct);

        verifyPermissions(query.deliveryId(), query.role(), query.entityCode());

        return DeliveryProductResponse.fromAggregate(deliveryProduct);
    }

    private void verifyDeliveryProduct(DeliveryProduct deliveryProduct) {
        if (deliveryProduct == null)
            throw new DeliveryProductNotFound();
    }

    private void verifyPermissions(Long deliveryId, Roles role, Long entityCode) {
        deliverySearcher.search(new DeliverySearcherQuery(
                deliveryId, role, entityCode
        ));
    }

}
