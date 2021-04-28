package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryStatusNameInvalid extends DomainError {

    public DeliveryStatusNameInvalid() {
        super("delivery_status_name_invalid", "El nombre del estado de la entrega no es válido");
    }
}
