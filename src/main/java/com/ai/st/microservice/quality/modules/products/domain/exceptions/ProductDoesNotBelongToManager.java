package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ProductDoesNotBelongToManager extends DomainError {

    public ProductDoesNotBelongToManager() {
        super("product_does_not_belong_to_manager", "El producto no pertenece al gestor.");
    }
}
