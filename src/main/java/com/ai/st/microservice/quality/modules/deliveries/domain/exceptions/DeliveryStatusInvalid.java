package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryStatusInvalid extends DomainError {

    public DeliveryStatusInvalid() {
        super("delivery_status_invalid", "El estado de la entrega no es válido");
    }
}
