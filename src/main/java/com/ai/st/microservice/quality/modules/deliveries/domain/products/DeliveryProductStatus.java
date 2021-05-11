package com.ai.st.microservice.quality.modules.deliveries.domain.products;

public final class DeliveryProductStatus {

    private final DeliveryProductStatusId id;
    private final DeliveryProductStatusName name;

    public DeliveryProductStatus(DeliveryProductStatusId id, DeliveryProductStatusName name) {
        this.id = id;
        this.name = name;
    }

    public static DeliveryProductStatus fromPrimitives(Long id, String name) {
        return new DeliveryProductStatus(new DeliveryProductStatusId(id), new DeliveryProductStatusName(name));
    }

    public DeliveryProductStatusId id() {
        return id;
    }

    public DeliveryProductStatusName name() {
        return name;
    }

}
