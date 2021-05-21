package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FTPDomainInvalid extends DomainError {

    public FTPDomainInvalid() {
        super("ftp_domain_invalid", "El dominio del servidor FTP no es válido");
    }
}
