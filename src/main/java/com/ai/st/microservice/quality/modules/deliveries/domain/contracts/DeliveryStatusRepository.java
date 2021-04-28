package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatus;

import java.util.List;

public interface DeliveryStatusRepository {

    void save(DeliveryStatus deliveryStatus);

    List<DeliveryStatus> all();

}
