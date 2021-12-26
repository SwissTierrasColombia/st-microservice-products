package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ErrorFromInfrastructure extends DomainError {

    public ErrorFromInfrastructure() {
        super("wrong_response_from_infrastructure", "No se ha podido consultar la información.");
    }

}
