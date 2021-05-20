package com.ai.st.microservice.quality.modules.products.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class ProductDateInvalid extends DomainError {

    public ProductDateInvalid() {
        super("product_date_invalid", "La fecha del producto no es válida.");
    }

}
