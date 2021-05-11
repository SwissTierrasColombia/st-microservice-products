package com.ai.st.microservice.quality.modules.deliveries.application.FindProductsFromDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;

public final class FindProductsFromDeliveryQuery {

    private final Long deliveryId;
    private final Roles role;
    private final Long entityCode;

    public FindProductsFromDeliveryQuery(Long deliveryId, Roles role, Long entityCode) {
        this.deliveryId = deliveryId;
        this.role = role;
        this.entityCode = entityCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

}
