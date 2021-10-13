package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryDateInvalid;

import java.util.Date;

public final class DeliveryStatusDate {

    private final Date value;

    public DeliveryStatusDate(Date value) {
        validateDate(value);
        this.value = value;
    }

    private void validateDate(Date value) {
        if (value == null) {
            throw new DeliveryDateInvalid();
        }
    }

    public Date value() {
        return value;
    }

}
