package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FTPPasswordInvalid extends DomainError {

    public FTPPasswordInvalid() {
        super("ftp_password_invalid", "La contraseña del servidor FTP no es válida.");
    }
}
