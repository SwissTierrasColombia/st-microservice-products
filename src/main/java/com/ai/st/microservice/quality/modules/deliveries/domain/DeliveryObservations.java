package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryObservationsInvalid;

import java.util.Objects;

public class DeliveryObservations {

    private final String value;

    public DeliveryObservations(String value) {
        ensureObservations(value);
        this.value = value;
    }

    private void ensureObservations(String value) {
        if (value == null) throw new DeliveryObservationsInvalid();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryObservations that = (DeliveryObservations) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
