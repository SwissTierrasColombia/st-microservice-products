package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryDateInvalid;

import java.util.Date;

public final class DeliveryDate {

    private final Date value;

    public DeliveryDate(Date value) {
        ensureFormat(value);
        this.value = value;
    }

    private void ensureFormat(Date value) {
        if (value == null) {
            throw new DeliveryDateInvalid();
        }
    }

    public Date value() {
        return value;
    }

}
