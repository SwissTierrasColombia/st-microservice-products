package com.ai.st.microservice.quality.modules.deliveries.application;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatus;

public final class DeliveryStatusResponse {

    private final Long id;
    private final String name;

    public DeliveryStatusResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DeliveryStatusResponse fromAggregate(DeliveryStatus deliveryStatus) {
        return new DeliveryStatusResponse(deliveryStatus.id().value(), deliveryStatus.name().value());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
