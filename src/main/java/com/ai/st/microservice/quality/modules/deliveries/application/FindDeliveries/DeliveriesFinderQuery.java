package com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.shared.application.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DeliveriesFinderQuery implements Query {

    private final int page;
    private final int limit;
    private final List<Long> states;
    private final String code;
    private final String municipality;
    private final Long operator;
    private final Long manager;
    private final Roles role;
    private final Long entityCode;

    public DeliveriesFinderQuery(int page, int limit, List<Long> states, String code,
                                 String municipality, Long operator, Long manager, Roles role, Long entityCode) {
        this.page = page;
        this.limit = limit;
        this.code = code;
        this.municipality = municipality;
        this.operator = operator;
        this.manager = manager;
        this.role = role;
        this.entityCode = entityCode;
        this.states = Objects.requireNonNullElseGet(states, ArrayList::new);
    }

    public List<Long> states() {
        return states;
    }

    public String code() {
        return code;
    }

    public String municipality() {
        return municipality;
    }

    public Long operator() {
        return operator;
    }

    public Long manager() {
        return manager;
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
