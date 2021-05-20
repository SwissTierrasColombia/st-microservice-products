package com.ai.st.microservice.quality.modules.products.domain;

import java.util.Objects;

public final class ProductXTF {

    private final boolean value;

    public ProductXTF(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }

    public static ProductXTF fromValue(boolean value) {
        return new ProductXTF(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductXTF that = (ProductXTF) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


