package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class OperatorInvalid extends DomainError {

    public OperatorInvalid() {
        super("operator_code_invalid", "El operador no es válido");
    }

}
