package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FTPPortInvalid extends DomainError {

    public FTPPortInvalid() {
        super("ftp_port_invalid", "El puerto del servidor FTP no es válido.");
    }
}
