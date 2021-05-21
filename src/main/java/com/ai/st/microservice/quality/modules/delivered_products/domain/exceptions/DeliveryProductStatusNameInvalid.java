package com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryProductStatusNameInvalid extends DomainError {

    public DeliveryProductStatusNameInvalid() {
        super("delivery_product_status_name_invalid", "El nombre del estado del producto de la entrega no es válido");
    }
}
