package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FTPUsernameInvalid extends DomainError {

    public FTPUsernameInvalid() {
        super("ftp_username_invalid", "El nombre de usuario del servidor FTP no es válido");
    }
}
