package com.ai.st.microservice.quality.modules.deliveries.domain.products;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryProductStatusNameInvalid;

public final class DeliveryProductStatusName {

    private final String value;

    public DeliveryProductStatusName(String value) {
        ensureStatusName(value);
        this.value = value;
    }

    private void ensureStatusName(String value) {
        if (value == null) throw new DeliveryProductStatusNameInvalid();
    }

    public String value() {
        return value;
    }

}
