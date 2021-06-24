package com.ai.st.microservice.quality.modules.deliveries.domain;

public final class DeliveryStatus {

    private final DeliveryStatusId id;
    private final DeliveryStatusName name;

    public DeliveryStatus(DeliveryStatusId id, DeliveryStatusName name) {
        this.id = id;
        this.name = name;
    }

    public static DeliveryStatus fromPrimitives(Long id, String name) {
        return new DeliveryStatus(new DeliveryStatusId(id), new DeliveryStatusName(name));
    }

    public DeliveryStatusId id() {
        return id;
    }

    public DeliveryStatusName name() {
        return name;
    }

}
