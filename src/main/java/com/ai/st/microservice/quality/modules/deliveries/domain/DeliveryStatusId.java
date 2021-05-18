package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryStatusInvalid;

public final class DeliveryStatusId {

    private final Long value;

    public static final Long DRAFT = (long) 1;
    public static final Long DELIVERED = (long) 2;
    public static final Long IN_VALIDATION = (long) 3;
    public static final Long ACCEPTED = (long) 4;
    public static final Long REJECTED = (long) 5;

    public DeliveryStatusId(Long value) {
        ensureStatus(value);
        this.value = value;
    }

    public static DeliveryStatusId fromValue(Long value) {
        return new DeliveryStatusId(value);
    }

    private void ensureStatus(Long value) {
        if (!value.equals(DeliveryStatusId.DRAFT)
                && !value.equals(DeliveryStatusId.DELIVERED)
                && !value.equals(DeliveryStatusId.IN_VALIDATION)
                && !value.equals(DeliveryStatusId.ACCEPTED)
                && !value.equals(DeliveryStatusId.REJECTED))
            throw new DeliveryStatusInvalid();
    }

    public Long value() {
        return value;
    }

}
