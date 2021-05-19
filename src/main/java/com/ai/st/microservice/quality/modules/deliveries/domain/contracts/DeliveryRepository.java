package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.quality.modules.shared.domain.criteria.Criteria;

public interface DeliveryRepository {

    void save(Delivery delivery);

    Delivery search(DeliveryId deliveryId);

    PageableDomain<Delivery> matching(Criteria criteria);

    void remove(DeliveryId deliveryId);

    void update(Delivery delivery);

    void changeState(DeliveryId deliveryId, DeliveryStatusId deliveryStatusId);

}
