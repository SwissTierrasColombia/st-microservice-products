package com.ai.st.microservice.quality.modules.feedbacks.domain.contracts;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;

public interface DeliveryProductFeedbackRepository {

    void save(DeliveryProductId deliveryProductId, Feedback feedback);

}
