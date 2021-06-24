package com.ai.st.microservice.quality.modules.shared.domain;

import java.util.Date;
import java.util.Objects;

public abstract class DateObjectValue {

    private final Date value;

    public DateObjectValue(Date value) {
        this.value = value;
    }

    public Date value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateObjectValue)) {
            return false;
        }
        DateObjectValue that = (DateObjectValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
