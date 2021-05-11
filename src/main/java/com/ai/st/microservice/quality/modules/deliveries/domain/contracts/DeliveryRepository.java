package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;

public interface DeliveryRepository {

    void save(Delivery delivery);

}
