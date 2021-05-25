package com.ai.st.microservice.quality.modules.feedbacks.domain.contracts;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;
import com.ai.st.microservice.quality.modules.feedbacks.domain.FeedbackId;

import java.util.List;

public interface DeliveryProductFeedbackRepository {

    Feedback search(FeedbackId feedbackId);

    List<Feedback> findByDeliveryProductId(DeliveryProductId deliveryProductId);

    void save(Feedback feedback);

}
