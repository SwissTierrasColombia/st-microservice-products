package com.ai.st.microservice.quality.modules.deliveries.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryProductStatus that = (DeliveryProductStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
