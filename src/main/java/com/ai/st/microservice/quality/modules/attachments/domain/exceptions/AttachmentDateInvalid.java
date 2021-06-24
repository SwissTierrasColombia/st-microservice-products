package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class AttachmentDateInvalid extends DomainError {

    public AttachmentDateInvalid() {
        super("attachment_date_invalid", "La fecha del adjunto no es válida.");
    }
}
