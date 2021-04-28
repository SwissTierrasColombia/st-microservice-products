package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.MunicipalityCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class Delivery {

    private final DeliveryId id;
    private final ManagerCode manager;
    private final OperatorCode operator;
    private final MunicipalityCode municipality;
    private final DeliveryObservations observations;
    private final UserCode user;
    private final Date createdAt;


    public Delivery(DeliveryId id, MunicipalityCode municipalityCode, ManagerCode manager, OperatorCode operatorCode,
                    UserCode user, DeliveryObservations observations, Date createdAt) {
        this.id = id;
        this.manager = manager;
        this.operator = operatorCode;
        this.municipality = municipalityCode;
        this.observations = observations;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return id.equals(delivery.id) && manager.equals(delivery.manager) && operator.equals(delivery.operator) && municipality.equals(delivery.municipality) && observations.equals(delivery.observations) && user.equals(delivery.user) && createdAt.equals(delivery.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manager, operator, municipality, observations, user, createdAt);
    }

    public DeliveryId id() {
        return id;
    }

    public ManagerCode manager() {
        return manager;
    }

    public OperatorCode operator() {
        return operator;
    }

    public MunicipalityCode municipality() {
        return municipality;
    }

    public DeliveryObservations observations() {
        return observations;
    }

    public UserCode user() {
        return user;
    }

    public Date createdAt() {
        return createdAt;
    }
}
