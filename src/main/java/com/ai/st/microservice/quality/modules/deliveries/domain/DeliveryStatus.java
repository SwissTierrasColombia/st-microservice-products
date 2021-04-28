package com.ai.st.microservice.quality.modules.deliveries.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryStatus that = (DeliveryStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
