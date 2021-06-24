package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.OperatorInvalid;

import java.util.Objects;

public final class OperatorCode {

    private final Long value;

    public OperatorCode(Long value) {
        ensureValidCode(value);

        this.value = value;
    }

    public Long value() {
        return value;
    }

    private void ensureValidCode(Long value) {
        if (value == null || value <= 0) throw new OperatorInvalid();
    }

    public static OperatorCode fromValue(Long value) {
        return new OperatorCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperatorCode that = (OperatorCode) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
