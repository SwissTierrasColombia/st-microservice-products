package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductIdInvalid;

import java.util.Objects;

public final class ProductId {

    private final Long value;

    public ProductId(Long value) {
        ensureProductId(value);
        this.value = value;
    }

    private void ensureProductId(Long value) {
        if (value == null || value <= 0) throw new ProductIdInvalid();
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
