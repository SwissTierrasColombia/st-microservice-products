package com.ai.st.microservice.quality.modules.shared.domain;

import java.util.Objects;

public abstract class LongObjectValue {

    private final Long value;

    public LongObjectValue(Long value) {
        this.value = value;
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongObjectValue)) {
            return false;
        }
        LongObjectValue that = (LongObjectValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
