package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ManagerNotFound extends DomainError {

    public ManagerNotFound() {
        super("manager_not_found", "No se ha encontrado el gestor.");
    }
}
