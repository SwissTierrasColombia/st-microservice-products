package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryDateInvalid extends DomainError {

    public DeliveryDateInvalid() {
        super("delivery_date_invalid", "La fecha de la entrega es inválida");
    }
}
