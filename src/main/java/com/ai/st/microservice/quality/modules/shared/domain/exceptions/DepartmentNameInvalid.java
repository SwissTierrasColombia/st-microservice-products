package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DepartmentNameInvalid extends DomainError {

    public DepartmentNameInvalid() {
        super("department_name_invalid", "El nombre del departamento no es válido.");
    }

}
