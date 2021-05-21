package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DocumentUrlInvalid extends DomainError {

    public DocumentUrlInvalid() {
        super("document_url_invalid", "La URL del documento no es válida.");
    }
}
