package com.ai.st.microservice.quality.modules.delivered_products.application;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatus;

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
