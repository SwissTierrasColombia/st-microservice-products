package com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryProductStatusInvalid extends DomainError {

    public DeliveryProductStatusInvalid() {
        super("delivery_product_status_invalid", "El estado de producto de entrega no es válido");
    }
}
