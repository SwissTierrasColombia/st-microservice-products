package com.ai.st.microservice.quality.modules.delivered_products.domain;

import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductDateInvalid;

import java.util.Date;

public final class DeliveryProductDate {

    private final Date value;

    public DeliveryProductDate(Date value) {
        ensureDate(value);
        this.value = value;
    }

    private void ensureDate(Date value) {
        if (value == null) {
            throw new DeliveryProductDateInvalid();
        }
    }

    public Date value() {
        return value;
    }

}
