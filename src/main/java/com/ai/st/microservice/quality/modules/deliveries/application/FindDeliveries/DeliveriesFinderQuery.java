package com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;

public final class DeliveriesFinderQuery {

    private final int page;
    private final int limit;
    private final Long stateId;
    private final Roles role;
    private final Long entityCode;

    public DeliveriesFinderQuery(int page, int limit, Long stateId, Roles role, Long entityCode) {
        this.page = page;
        this.limit = limit;
        this.stateId = stateId;
        this.role = role;
        this.entityCode = entityCode;
    }

    public Long stateId() {
        return stateId;
    }

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

    public int page() {
        return page;
    }

    public int limit() {
        return limit;
    }

}
