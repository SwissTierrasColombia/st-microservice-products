package com.ai.st.microservice.quality.modules.attachments.domain;

import java.util.Objects;

public final class DeliveryProductAttachmentObservations {

    private final String value;

    public DeliveryProductAttachmentObservations(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryProductAttachmentObservations that = (DeliveryProductAttachmentObservations) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
