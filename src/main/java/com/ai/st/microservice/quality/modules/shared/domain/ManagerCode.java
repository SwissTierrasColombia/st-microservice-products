package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ManagerInvalid;

import java.util.Objects;

public final class ManagerCode {

    private final Long value;

    public ManagerCode(Long value) {
        ensureValidCode(value);
        this.value = value;
    }

    private void ensureValidCode(Long value) {
        if (value == null || value <= 0) throw new ManagerInvalid();
    }

    public static ManagerCode fromValue(Long value) {
        return new ManagerCode(value);
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerCode that = (ManagerCode) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
