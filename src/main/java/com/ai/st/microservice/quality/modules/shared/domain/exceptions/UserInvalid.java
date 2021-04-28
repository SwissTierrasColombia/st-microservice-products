package com.ai.st.microservice.quality.modules.shared.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class UserInvalid extends DomainError {

    public UserInvalid() {
        super("user_code_invalid", "El usuario no es válido");
    }
}
