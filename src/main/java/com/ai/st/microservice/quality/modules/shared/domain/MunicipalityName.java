package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.MunicipalityNameInvalid;

public final class MunicipalityName extends StringValueObject {

    private MunicipalityName(String value) {
        super(value);
    }

    private static void ensureName(String value) {
        if (value == null || value.isEmpty())
            throw new MunicipalityNameInvalid();
    }

    public static MunicipalityName fromValue(String value) {
        ensureName(value);
        return new MunicipalityName(value);
    }

}
