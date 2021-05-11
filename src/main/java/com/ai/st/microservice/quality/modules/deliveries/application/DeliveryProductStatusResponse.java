package com.ai.st.microservice.quality.modules.deliveries.application;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatus;

public final class DeliveryProductStatusResponse {

    private final Long id;
    private final String name;

    public DeliveryProductStatusResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static DeliveryProductStatusResponse fromAggregate(DeliveryProductStatus deliveryProductStatus) {
        return new DeliveryProductStatusResponse(deliveryProductStatus.id().value(), deliveryProductStatus.name().value());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
