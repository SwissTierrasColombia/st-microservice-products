package com.ai.st.microservice.quality.modules.attachments.domain;

import java.util.Objects;

public final class DeliveryProductAttachmentId {

    private final Long value;

    public DeliveryProductAttachmentId(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryProductAttachmentId that = (DeliveryProductAttachmentId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
