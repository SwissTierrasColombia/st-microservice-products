package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class AttachmentNotFound extends DomainError {

    public AttachmentNotFound() {
        super("attachment_not_found", "No se ha encontrado el adjunto.");
    }
}
