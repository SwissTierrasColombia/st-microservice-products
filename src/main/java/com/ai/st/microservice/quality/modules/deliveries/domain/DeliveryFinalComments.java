package com.ai.st.microservice.quality.modules.deliveries.domain;

public class DeliveryFinalComments {

    private final String value;

    public DeliveryFinalComments(String value) {
        this.value = value;
    }

    public static DeliveryFinalComments fromValue(String value) {
        return new DeliveryFinalComments(value);
    }

    public String value() {
        return value;
    }

}
