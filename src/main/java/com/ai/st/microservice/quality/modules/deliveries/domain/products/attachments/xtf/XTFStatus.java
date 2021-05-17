package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.XTFStatusInvalid;

public final class XTFStatus {

    private final Status value;

    public enum Status {
        IN_VALIDATION,
        ACCEPTED,
        REJECTED
    }

    public XTFStatus(Status value) {
        ensureStatus(value);
        this.value = value;
    }

    public static Status valueOf(String value) {
        switch (value) {
            case "ACCEPTED":
                return Status.ACCEPTED;
            case "REJECTED":
                return Status.REJECTED;
            case "IN_VALIDATION":
            default:
                return Status.IN_VALIDATION;
        }
    }

    private void ensureStatus(Status value) {
        if (value == null) throw new XTFStatusInvalid();
    }

    public Status value() {
        return value;
    }

}
