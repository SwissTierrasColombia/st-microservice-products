package com.ai.st.microservice.quality.modules.attachments.domain;

import java.util.UUID;

public final class DeliveryProductAttachmentUUID {

    private final String value;

    public DeliveryProductAttachmentUUID(String value) {
        ensureUUID(value);
        this.value = value;
    }

    public String value() {
        return value;
    }

    private void ensureUUID(String value) throws IllegalArgumentException {
        UUID.fromString(value);
    }

}
