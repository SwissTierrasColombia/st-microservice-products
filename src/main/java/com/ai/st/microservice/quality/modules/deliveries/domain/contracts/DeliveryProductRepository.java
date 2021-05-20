package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;

import java.util.List;

public interface DeliveryProductRepository {

    List<DeliveryProduct> findByDeliveryId(DeliveryId deliveryId);

    void save(DeliveryId deliveryId, DeliveryProduct deliveryProduct);

    DeliveryProduct search(DeliveryProductId deliveryProductId);

    void remove(DeliveryProductId deliveryProductId);

    void update(DeliveryProduct deliveryProduct);

    List<DeliveryProduct> findByProductId(ProductId productId);

}
