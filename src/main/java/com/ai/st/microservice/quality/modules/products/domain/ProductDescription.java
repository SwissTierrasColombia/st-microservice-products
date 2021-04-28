package com.ai.st.microservice.quality.modules.products.domain;

import java.util.Objects;

public final class ProductDescription {

    private final String value;

    public ProductDescription(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDescription that = (ProductDescription) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
