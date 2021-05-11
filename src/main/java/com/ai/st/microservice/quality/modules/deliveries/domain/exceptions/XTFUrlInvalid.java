package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class XTFUrlInvalid extends DomainError {

    public XTFUrlInvalid() {
        super("xtf_url_invalid", "La URL del XTF no es válida");
    }
}
