package com.ai.st.microservice.quality.modules.deliveries.domain;

public final class DeliveryId {

    private final Long value;

    public DeliveryId(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

}
