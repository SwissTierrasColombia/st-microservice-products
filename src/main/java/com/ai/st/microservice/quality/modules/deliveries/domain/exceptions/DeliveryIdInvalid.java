package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public class DeliveryIdInvalid extends DomainError {

    public DeliveryIdInvalid() {
        super("delivery_id_invalid", "El ID de la entrega es inválido");
    }
}
