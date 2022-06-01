package com.ai.st.microservice.quality.modules.attachments.domain.ftp;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.FTPPortInvalid;

public final class FTPPort {

    private final String value;

    public FTPPort(String value) {
        ensurePort(value);
        this.value = value;
    }

    private void ensurePort(String value) {
        if (value == null || value.isEmpty())
            throw new FTPPortInvalid();
    }

    public String value() {
        return value;
    }

}
