package com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ProductHasBeenAlreadyAddedToDelivery extends DomainError {

    public ProductHasBeenAlreadyAddedToDelivery() {
        super("product_has_been_already_added_to_delivery", "El producto ya ha sido agregado a la entrega.");
    }
}
