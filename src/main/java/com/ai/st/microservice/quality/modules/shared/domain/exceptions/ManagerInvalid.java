package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ManagerInvalid extends DomainError {

    public ManagerInvalid() {
        super("manager_code_invalid", "El gestor no es válido");
    }

}
