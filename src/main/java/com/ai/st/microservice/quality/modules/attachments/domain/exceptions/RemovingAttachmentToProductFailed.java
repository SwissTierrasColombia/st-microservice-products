package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class RemovingAttachmentToProductFailed extends DomainError {

    public RemovingAttachmentToProductFailed(String errorMessage) {
        super("removing_attachment_to_product_failed", errorMessage);
    }
}
