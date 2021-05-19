package com.ai.st.microservice.quality.modules.deliveries.domain.products;

public final class DeliveryProductObservations {

    private final String value;

    public DeliveryProductObservations(String value) {
        this.value = value;
    }

    public static DeliveryProductObservations fromValue(String value) {
        return new DeliveryProductObservations(value);
    }

    public String value() {
        return value;
    }

}
