package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryCodeInvalid extends DomainError {

    public DeliveryCodeInvalid() {
        super("delivery_code_invalid", "El código de la entrega no es válido.");
    }
}
