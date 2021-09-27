package com.ai.st.microservice.quality.modules.attachments.domain.xtf;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.XTFReportRevisionUrlInvalid;

public final class XTFReportRevisionUrl {

    private final String value;

    public XTFReportRevisionUrl(String value, boolean skipValidation) {
        if (!skipValidation) {
            ensureUrl(value);
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    private void ensureUrl(String value) {
        if (value == null || value.isEmpty()) throw new XTFReportRevisionUrlInvalid(value);
    }

}
