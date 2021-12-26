package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class MunicipalityNameInvalid extends DomainError {

    public MunicipalityNameInvalid() {
        super("municipality_name_invalid", "El nombre del municipio no es válido.");
    }

}
