package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class UnauthorizedToRemoveProduct extends DomainError {

    public UnauthorizedToRemoveProduct() {
        super("unauthorized_to_remove_product", "Sin autorización para eliminar el producto.");
    }

    public UnauthorizedToRemoveProduct(String errorMessage) {
        super("unauthorized_to_remove_product", errorMessage);
    }

}
