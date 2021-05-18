package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;

import java.util.List;

public interface DeliveryProductRepository {

    List<DeliveryProduct> findProductsFromDelivery(DeliveryId deliveryId);

    void save(DeliveryId deliveryId, DeliveryProduct deliveryProduct);

    DeliveryProduct search(DeliveryProductId deliveryProductId);

    void remove(DeliveryProductId deliveryProductId);

}
