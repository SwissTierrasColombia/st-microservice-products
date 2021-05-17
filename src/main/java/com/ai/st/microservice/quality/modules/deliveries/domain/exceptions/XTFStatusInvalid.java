package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class XTFStatusInvalid extends DomainError {

    public XTFStatusInvalid() {
        super("xtf_status_invalid", "El estado del archivo XTF no es válido.");
    }
}
