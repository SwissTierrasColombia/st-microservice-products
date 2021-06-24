package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class NumberAttachmentsExceeded extends DomainError {

    public NumberAttachmentsExceeded(int maximumAttachments) {
        super("number_attachments_exceeded", String.format("Máximo se puede cargar %d adjuntos por producto.", maximumAttachments));
    }

}
