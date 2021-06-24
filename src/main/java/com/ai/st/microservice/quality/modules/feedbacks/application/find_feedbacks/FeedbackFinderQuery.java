package com.ai.st.microservice.quality.modules.feedbacks.application.find_feedbacks;

import com.ai.st.microservice.quality.modules.shared.application.Query;
import com.ai.st.microservice.quality.modules.shared.application.Roles;

public final class FeedbackFinderQuery implements Query {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Roles role;
    private final Long entityCode;

    public FeedbackFinderQuery(Long deliveryId, Long deliveryProductId, Roles role, Long entityCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.role = role;
        this.entityCode = entityCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

}
