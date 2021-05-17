package com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;

public final class DeliveryProductSearcherQuery {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Roles role;
    private final Long entityCode;

    public DeliveryProductSearcherQuery(Long deliveryId, Long deliveryProductId, Roles role, Long entityCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.role = role;
        this.entityCode = entityCode;
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

    public Long deliveryId() {
        return deliveryId;
    }
}
