package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class MicroserviceUnreachable extends DomainError {

    public MicroserviceUnreachable(String microserviceName) {
        super("microservice_unreachable", "No se ha podido alcanzar conexión con el microservicio " + microserviceName);
    }

}
