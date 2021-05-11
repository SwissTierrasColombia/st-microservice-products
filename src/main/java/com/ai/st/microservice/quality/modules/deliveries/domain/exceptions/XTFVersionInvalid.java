package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class XTFVersionInvalid extends DomainError {

    public XTFVersionInvalid() {
        super("xtf_version_invalid", "La versión del XTF no es válida.");
    }
}
