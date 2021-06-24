package com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryProductNotFound extends DomainError {

    public DeliveryProductNotFound() {
        super("delivery_product_not_found", "No se ha encontrado el producto asociado a la entrega.");
    }

}
