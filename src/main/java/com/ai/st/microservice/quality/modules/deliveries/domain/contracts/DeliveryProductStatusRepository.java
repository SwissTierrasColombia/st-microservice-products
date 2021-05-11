package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatus;

import java.util.List;

public interface DeliveryProductStatusRepository {

    void save(DeliveryProductStatus deliveryProductStatus);

    List<DeliveryProductStatus> all();

}
