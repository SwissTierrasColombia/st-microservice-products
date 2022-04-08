package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDateInvalid;

import java.util.Date;

public final class ProductDate {

    private final Date value;

    public ProductDate(Date value) {
        ensureDate(value);
        this.value = value;
    }

    private void ensureDate(Date value) {
        if (value == null)
            throw new ProductDateInvalid();
    }

    public static ProductDate fromValue(Date value) {
        return new ProductDate(value);
    }

    public Date value() {
        return value;
    }

}
