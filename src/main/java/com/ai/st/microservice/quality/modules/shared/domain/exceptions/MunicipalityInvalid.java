package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public class MunicipalityInvalid extends DomainError {

    public MunicipalityInvalid() {
        super("municipality_code_invalid", "El municipio no es válido");
    }
}
