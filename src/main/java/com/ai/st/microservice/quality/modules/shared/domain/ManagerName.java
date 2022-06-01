package com.ai.st.microservice.quality.modules.shared.domain;

import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ManagerNameInvalid;

public final class ManagerName extends StringValueObject {

    private ManagerName(String value) {
        super(value);
    }

    private static void ensureName(String value) {
        if (value == null || value.isEmpty())
            throw new ManagerNameInvalid();
    }

    public static ManagerName fromValue(String value) {
        ensureName(value);
        return new ManagerName(value);
    }

}
