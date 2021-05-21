package com.ai.st.microservice.quality.modules.attachments.application.find_attachments_from_product;

import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.shared.application.Query;

public final class AttachmentsProductFinderQuery implements Query {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Roles role;
    private final Long entityCode;

    public AttachmentsProductFinderQuery(Long deliveryId, Long deliveryProductId, Roles role, Long entityCode) {
        this.deliveryProductId = deliveryProductId;
        this.role = role;
        this.deliveryId = deliveryId;
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
