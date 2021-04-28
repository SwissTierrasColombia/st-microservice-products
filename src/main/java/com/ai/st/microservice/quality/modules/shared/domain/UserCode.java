package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.UserInvalid;

import java.util.Objects;

public final class UserCode {

    private final Long value;

    public UserCode(Long value) {
        ensureUser(value);
        this.value = value;
    }

    private void ensureUser(Long value) {
        if (value == null || value <= 0) throw new UserInvalid();
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCode userCode = (UserCode) o;
        return Objects.equals(value, userCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
