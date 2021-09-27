package com.ai.st.microservice.quality.modules.attachments.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class XTFReportRevisionUrlInvalid extends DomainError {

    public XTFReportRevisionUrlInvalid(String reportRevisionURL) {
        super("xtf_url_invalid", String.format("La URL %s del reporte no es válida", reportRevisionURL));
    }
}
