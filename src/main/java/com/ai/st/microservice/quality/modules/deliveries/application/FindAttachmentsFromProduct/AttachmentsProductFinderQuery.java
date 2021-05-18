package com.ai.st.microservice.quality.modules.deliveries.application.FindAttachmentsFromProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;

public final class AttachmentsProductFinderQuery {

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
