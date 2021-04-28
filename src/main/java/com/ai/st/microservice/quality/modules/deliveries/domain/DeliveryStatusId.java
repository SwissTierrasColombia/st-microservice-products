package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryStatusInvalid;

import java.util.Objects;

public final class DeliveryStatusId {

    private final Long value;

    public static final Long IN_VALIDATION = (long) 1;
    public static final Long DELIVERED = (long) 2;
    public static final Long REJECT = (long) 3;

    public DeliveryStatusId(Long value) {
        ensureStatus(value);
        this.value = value;
    }

    private void ensureStatus(Long value) {
        if (!value.equals(DeliveryStatusId.IN_VALIDATION) && !value.equals(DeliveryStatusId.DELIVERED) && !value.equals(DeliveryStatusId.REJECT))
            throw new DeliveryStatusInvalid();
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryStatusId that = (DeliveryStatusId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
