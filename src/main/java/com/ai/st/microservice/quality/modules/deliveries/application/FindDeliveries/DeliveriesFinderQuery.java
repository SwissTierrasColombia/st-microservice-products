package com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DeliveriesFinderQuery {

    private final int page;
    private final int limit;
    private final List<Long> states;
    private final Roles role;
    private final Long entityCode;

    public DeliveriesFinderQuery(int page, int limit, List<Long> states, Roles role, Long entityCode) {
        this.page = page;
        this.limit = limit;
        this.role = role;
        this.entityCode = entityCode;
        this.states = Objects.requireNonNullElseGet(states, ArrayList::new);
    }

    public List<Long> states() {
        return states;
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
