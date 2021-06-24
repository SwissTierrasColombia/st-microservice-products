package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductNameInvalid;

import java.util.Objects;

public final class ProductName {

    private final String value;

    public ProductName(String value) {
        ensureProductName(value);
        this.value = value;
    }

    private void ensureProductName(String value) {
        if (value == null || value.length() <= 0) throw new ProductNameInvalid();
    }

    public static ProductName fromValue(String value) {
        return new ProductName(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductName that = (ProductName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
