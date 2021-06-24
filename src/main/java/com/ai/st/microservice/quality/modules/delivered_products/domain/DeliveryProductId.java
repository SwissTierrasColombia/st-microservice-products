package com.ai.st.microservice.quality.modules.delivered_products.domain;

public final class DeliveryProductId {

    private final Long value;

    public DeliveryProductId(Long value) {
        this.value = value;
    }

    public static DeliveryProductId fromValue(Long value) {
        return new DeliveryProductId(value);
    }

    public Long value() {
        return value;
    }

}
