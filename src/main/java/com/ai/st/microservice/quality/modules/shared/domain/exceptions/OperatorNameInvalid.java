package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class OperatorNameInvalid extends DomainError {

    public OperatorNameInvalid() {
        super("operator_name_invalid", "El nombre del operador no es válido.");
    }

}
