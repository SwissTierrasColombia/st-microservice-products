package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryIdInvalid;

import java.util.Objects;

public final class DeliveryId {

    private final Long value;

    public DeliveryId(Long value) {
        ensureDeliveryId(value);

        this.value = value;
    }

    private void ensureDeliveryId(Long value) {
        if (value == null || value <= 0) throw new DeliveryIdInvalid();
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryId that = (DeliveryId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
