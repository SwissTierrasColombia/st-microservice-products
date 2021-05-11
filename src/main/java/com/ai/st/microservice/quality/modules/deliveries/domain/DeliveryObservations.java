package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryObservationsInvalid;

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

}
