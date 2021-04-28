package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ProductNameInvalid extends DomainError {

    public ProductNameInvalid() {
        super("product_name_invalid", "El nombre del producto no es válido");
    }
}
