package com.ai.st.microservice.quality.modules.delivered_products.domain;

import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductStatusInvalid;

public final class DeliveryProductStatusId {

    private final Long value;

    public static final Long PENDING = (long) 1;
    public static final Long ACCEPTED = (long) 2;
    public static final Long REJECTED = (long) 3;

    public DeliveryProductStatusId(Long value) {
        ensureStatus(value);
        this.value = value;
    }

    public static DeliveryProductStatusId fromValue(Long value) {
        return new DeliveryProductStatusId(value);
    }

    private void ensureStatus(Long value) {
        if (!value.equals(DeliveryProductStatusId.PENDING) && !value.equals(DeliveryProductStatusId.ACCEPTED)
                && !value.equals(DeliveryProductStatusId.REJECTED))
            throw new DeliveryProductStatusInvalid();
    }

    public Long value() {
        return value;
    }

}
