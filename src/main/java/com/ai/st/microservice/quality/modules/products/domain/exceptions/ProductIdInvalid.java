package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ProductIdInvalid extends DomainError {

    public ProductIdInvalid() {
        super("product_id_invalid", "El código del producto no es válido");
    }
}
