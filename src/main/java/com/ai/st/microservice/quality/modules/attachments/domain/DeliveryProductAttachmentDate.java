package com.ai.st.microservice.quality.modules.attachments.domain;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentDateInvalid;

import java.util.Date;

public final class DeliveryProductAttachmentDate {

    private final Date value;

    public DeliveryProductAttachmentDate(Date value) {
        ensureDate(value);
        this.value = value;
    }

    private void ensureDate(Date value) {
        if (value == null) throw new AttachmentDateInvalid();
    }

    public Date value() {
        return value;
    }

}
