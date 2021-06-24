package com.ai.st.microservice.quality.modules.attachments.domain.xtf;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.XTFUrlInvalid;

import java.util.Objects;

public final class XTFUrl {

    private final String value;

    public XTFUrl(String value) {
        ensureUrl(value);
        this.value = value;
    }

    public String value() {
        return value;
    }

    private void ensureUrl(String value) {
        if (value == null || value.isEmpty()) throw new XTFUrlInvalid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XTFUrl xtfUrl = (XTFUrl) o;
        return Objects.equals(value, xtfUrl.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
