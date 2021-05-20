package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ProductNotFound extends DomainError {

    public ProductNotFound() {
        super("product_not_found", "No se ha encontrado el producto.");
    }
}
