package com.ai.st.microservice.quality.modules.delivered_products.domain;

import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductStatusNameInvalid;

public final class DeliveryProductStatusName {

    private final String value;

    public DeliveryProductStatusName(String value) {
        ensureStatusName(value);
        this.value = value;
    }

    private void ensureStatusName(String value) {
        if (value == null) throw new DeliveryProductStatusNameInvalid();
    }

    public static DeliveryProductStatusName fromValue(String value) {
        return new DeliveryProductStatusName(value);
    }

    public String value() {
        return value;
    }

}
