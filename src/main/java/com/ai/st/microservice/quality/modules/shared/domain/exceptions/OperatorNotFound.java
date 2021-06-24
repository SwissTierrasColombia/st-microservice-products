package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class OperatorNotFound extends DomainError {

    public OperatorNotFound() {
        super("operator_not_found", "No se ha encontrado el operador.");
    }
}
