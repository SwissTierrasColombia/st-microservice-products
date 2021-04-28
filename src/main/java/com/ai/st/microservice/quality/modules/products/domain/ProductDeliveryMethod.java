package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.products.domain.exceptions.DeliveryMethodInvalid;

import java.util.Objects;

public final class ProductDeliveryMethod {

    private final String value;

    public ProductDeliveryMethod(String value) {
        ensureDeliveryMethod(value);
        this.value = value;
    }

    private void ensureDeliveryMethod(String value) {
        if (!value.equals("XTF") && !value.equals("DOCUMENT") && !value.equals("FTP") && !value.equals("URL")
                && !value.equals("ANY")) throw new DeliveryMethodInvalid();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDeliveryMethod that = (ProductDeliveryMethod) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}


