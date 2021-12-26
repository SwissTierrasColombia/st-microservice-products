package com.ai.st.microservice.quality.modules.delivered_products.domain.contracts;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatus;

import java.util.List;

public interface DeliveryProductStatusRepository {

    void save(DeliveryProductStatus deliveryProductStatus);

    List<DeliveryProductStatus> all();

}
