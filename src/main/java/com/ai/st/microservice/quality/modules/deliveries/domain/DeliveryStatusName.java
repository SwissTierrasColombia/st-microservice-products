package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryStatusNameInvalid;

public final class DeliveryStatusName {

    private final String value;

    public DeliveryStatusName(String value) {
        ensureStatusName(value);
        this.value = value;
    }

    private void ensureStatusName(String value) {
        if (value == null) throw new DeliveryStatusNameInvalid();
    }

    public static DeliveryStatusName fromValue(String value) {
        return new DeliveryStatusName(value);
    }

    public String value() {
        return value;
    }

}
