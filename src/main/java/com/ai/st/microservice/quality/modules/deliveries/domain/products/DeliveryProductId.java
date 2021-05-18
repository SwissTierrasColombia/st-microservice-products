package com.ai.st.microservice.quality.modules.deliveries.domain.products;

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
