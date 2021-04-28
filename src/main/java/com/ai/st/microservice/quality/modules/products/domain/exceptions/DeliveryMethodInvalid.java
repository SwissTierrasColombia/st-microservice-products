package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryMethodInvalid extends DomainError {

    public DeliveryMethodInvalid() {
        super("delivery_method_invalid", "El método de entrega no es válido");
    }
}
