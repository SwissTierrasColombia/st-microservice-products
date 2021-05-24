package com.ai.st.microservice.quality.modules.feedbacks.domain.contracts;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;

import java.util.List;

public interface DeliveryProductFeedbackRepository {

    void save(DeliveryProductId deliveryProductId, Feedback feedback);

    List<Feedback> findByDeliveryProductId(DeliveryProductId deliveryProductId);

}
