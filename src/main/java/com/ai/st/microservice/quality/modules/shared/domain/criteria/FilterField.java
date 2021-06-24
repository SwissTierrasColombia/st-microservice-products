package com.ai.st.microservice.quality.modules.shared.domain.criteria;

public final class FilterField {

    private final String value;

    public FilterField(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
