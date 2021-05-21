package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class AddingAttachmentToProductFailed extends DomainError {

    public AddingAttachmentToProductFailed(String errorMessage) {
        super("adding_attachment_to_product_failed", errorMessage);
    }
}


