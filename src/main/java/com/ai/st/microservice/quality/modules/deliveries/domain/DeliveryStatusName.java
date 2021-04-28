package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryStatusNameInvalid;

import java.util.Objects;

public final class DeliveryStatusName {

    private final String value;

    public DeliveryStatusName(String value) {
        ensureStatusName(value);
        this.value = value;
    }

    private void ensureStatusName(String value) {
        if (value == null) throw new DeliveryStatusNameInvalid();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryStatusName that = (DeliveryStatusName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
