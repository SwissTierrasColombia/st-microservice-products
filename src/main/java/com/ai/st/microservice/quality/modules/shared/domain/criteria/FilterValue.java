package com.ai.st.microservice.quality.modules.shared.domain.criteria;

public final class FilterValue {

    private final String value;

    public FilterValue(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
