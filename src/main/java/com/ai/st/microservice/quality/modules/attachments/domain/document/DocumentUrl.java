package com.ai.st.microservice.quality.modules.attachments.domain.document;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.DocumentUrlInvalid;

public final class DocumentUrl {

    private final String value;

    public DocumentUrl(String value) {
        ensureUrl(value);
        this.value = value;
    }

    private void ensureUrl(String value) {
        if (value == null || value.isEmpty())
            throw new DocumentUrlInvalid();
    }

    public String value() {
        return value;
    }

}
