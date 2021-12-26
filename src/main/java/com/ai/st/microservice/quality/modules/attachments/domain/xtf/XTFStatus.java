package com.ai.st.microservice.quality.modules.attachments.domain.xtf;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.XTFStatusInvalid;

public final class XTFStatus {

    private final Status value;

    public enum Status {
        IN_VALIDATION,
        ACCEPTED,
        REJECTED,
        QUALITY_PROCESS_IN_VALIDATION,
        QUALITY_PROCESS_FINISHED,
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
            case "QUALITY_PROCESS_IN_VALIDATION":
                return Status.QUALITY_PROCESS_IN_VALIDATION;
            case "QUALITY_PROCESS_FINISHED":
                return Status.QUALITY_PROCESS_FINISHED;
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
