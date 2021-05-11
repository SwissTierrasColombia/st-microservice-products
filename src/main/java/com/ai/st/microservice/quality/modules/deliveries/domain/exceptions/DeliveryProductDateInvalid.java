package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryProductDateInvalid extends DomainError {

    public DeliveryProductDateInvalid() {
        super("delivery_product_date_invalid", "La fecha del producto de entrega no es válido");
    }
}
