package com.ai.st.microservice.quality.modules.attachments.domain.ftp;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.FTPUsernameInvalid;

public final class FTPUsername {

    private final String value;

    public FTPUsername(String value) {
        ensureUsername(value);
        this.value = value;
    }

    private void ensureUsername(String value) {
        if (value == null || value.isEmpty())
            throw new FTPUsernameInvalid();
    }

    public String value() {
        return value;
    }

}
