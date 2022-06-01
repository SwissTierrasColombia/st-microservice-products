package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.OperatorNameInvalid;

public final class OperatorName extends StringValueObject {

    private OperatorName(String value) {
        super(value);
    }

    private static void ensureName(String value) {
        if (value == null || value.isEmpty())
            throw new OperatorNameInvalid();
    }

    public static OperatorName fromValue(String value) {
        ensureName(value);
        return new OperatorName(value);
    }

}
