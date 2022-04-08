package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.MunicipalityInvalid;

import java.util.Objects;

public class MunicipalityCode {

    private final String value;

    public MunicipalityCode(String value) {
        ensureMunicipalityCode(value);
        this.value = value;
    }

    private void ensureMunicipalityCode(String value) {
        if (value == null || value.length() != 5)
            throw new MunicipalityInvalid();
    }

    public static MunicipalityCode fromValue(String value) {
        return new MunicipalityCode(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MunicipalityCode that = (MunicipalityCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
