package com.ai.st.microservice.quality.modules.feedbacks.application.get_feedback_url;

import com.ai.st.microservice.quality.modules.shared.application.Query;
import com.ai.st.microservice.quality.modules.shared.application.Roles;

public final class FeedbackURLGetterQuery implements Query {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long feedbackId;
    private final Roles role;
    private final Long entityCode;

    public FeedbackURLGetterQuery(Long deliveryId, Long deliveryProductId, Long feedbackId, Roles role, Long entityCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.feedbackId = feedbackId;
        this.role = role;
        this.entityCode = entityCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long feedbackId() {
        return feedbackId;
    }

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

}
