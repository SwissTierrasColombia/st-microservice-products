package com.ai.st.microservice.quality.modules.delivered_products.application.find_products_from_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.shared.application.Query;

public final class DeliveryProductsFinderQuery implements Query {

    private final Long deliveryId;
    private final Roles role;
    private final Long entityCode;

    public DeliveryProductsFinderQuery(Long deliveryId, Roles role, Long entityCode) {
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
