package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class AttachmentTypeNotSupportedToProduct extends DomainError {

    public AttachmentTypeNotSupportedToProduct() {
        super("attachment_type_not_supported_to_product", "El tipo de adjunto no esta soportado para el producto.");
    }

}
