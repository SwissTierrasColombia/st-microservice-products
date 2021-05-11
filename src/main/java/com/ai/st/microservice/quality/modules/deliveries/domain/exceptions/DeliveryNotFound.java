package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryNotFound extends DomainError {

    public DeliveryNotFound() {
        super("delivery_not_found", "No se ha encontrado la entrega.");
    }
}
