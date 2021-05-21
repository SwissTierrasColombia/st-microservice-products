package com.ai.st.microservice.quality.modules.attachments.domain.xtf;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.XTFVersionInvalid;

public final class XTFVersion {

    private final String value;

    public XTFVersion(String value) {
        ensureVersion(value);
        this.value = value;
    }

    private void ensureVersion(String value) {
        if (value == null || value.isEmpty()) throw new XTFVersionInvalid();
    }

    public String value() {
        return value;
    }

}
