package com.ai.st.microservice.quality.modules.attachments.domain.ftp;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.FTPPasswordInvalid;

public final class FTPPassword {

    private final String value;

    public FTPPassword(String value) {
        this.value = value;
    }

    private void ensurePassword(String value) {
        if (value == null || value.isEmpty())
            throw new FTPPasswordInvalid();
    }

    public String value() {
        return value;
    }
}
