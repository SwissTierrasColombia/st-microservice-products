package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class OperatorDoesNotBelongToManager extends DomainError {

    public OperatorDoesNotBelongToManager() {
        super("operator_does_not_belong_to_manager", "El operador no pertenece al gestor.");
    }
}
