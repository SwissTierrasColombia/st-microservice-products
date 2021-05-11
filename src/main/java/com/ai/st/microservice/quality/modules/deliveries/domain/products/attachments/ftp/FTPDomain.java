package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.ftp;

import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.FTPDomainInvalid;

public final class FTPDomain {

    private final String value;

    public FTPDomain(String value) {
        ensureDomain(value);
        this.value = value;
    }

    private void ensureDomain(String value) {
        if (value == null || value.isEmpty()) throw new FTPDomainInvalid();
    }

    public String value() {
        return value;
    }

}
