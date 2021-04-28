package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryProductStatusInvalid;

import java.util.Objects;

public final class DeliveryProductStatusId {

    private final Long value;

    public static final Long IN_VALIDATION = (long) 1;
    public static final Long ACCEPT = (long) 2;
    public static final Long REJECT = (long) 3;

    public DeliveryProductStatusId(Long value) {
        ensureStatus(value);
        this.value = value;
    }

    private void ensureStatus(Long value) {
        if (!value.equals(DeliveryProductStatusId.IN_VALIDATION) && !value.equals(DeliveryProductStatusId.ACCEPT) && !value.equals(DeliveryProductStatusId.REJECT))
            throw new DeliveryProductStatusInvalid();
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryProductStatusId that = (DeliveryProductStatusId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
