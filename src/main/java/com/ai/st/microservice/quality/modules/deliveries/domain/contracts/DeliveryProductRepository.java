package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;

import java.util.List;

public interface DeliveryProductRepository {

    List<DeliveryProduct> findProductsFromDelivery(DeliveryId deliveryId);

    void save(DeliveryId deliveryId, DeliveryProduct deliveryProduct);

}
