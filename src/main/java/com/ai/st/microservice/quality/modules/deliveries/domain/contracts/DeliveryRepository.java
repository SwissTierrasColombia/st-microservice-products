package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;

public interface DeliveryRepository {

    void save(Delivery delivery);

    Delivery search(DeliveryId id);

}
