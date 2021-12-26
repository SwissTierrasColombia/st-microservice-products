package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class AttachmentUnsupportedToDownload extends DomainError {

    public AttachmentUnsupportedToDownload() {
        super("attachment_unsupported_to_download", "El adjunto no soporta descargas.");
    }

}
