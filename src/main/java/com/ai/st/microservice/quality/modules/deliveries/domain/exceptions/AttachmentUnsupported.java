package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class AttachmentUnsupported extends DomainError {

    public AttachmentUnsupported() {
        super("attachment_unsupported", "Tipo de adjunto no soportado.");
    }
}
