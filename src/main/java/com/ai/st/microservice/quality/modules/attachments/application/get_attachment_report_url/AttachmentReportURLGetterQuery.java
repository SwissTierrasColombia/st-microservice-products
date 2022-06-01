package com.ai.st.microservice.quality.modules.attachments.application.get_attachment_report_url;

import com.ai.st.microservice.quality.modules.shared.application.Query;

public final class AttachmentReportURLGetterQuery implements Query {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long attachmentId;
    private final Long managerCode;

    public AttachmentReportURLGetterQuery(Long deliveryId, Long deliveryProductId, Long attachmentId,
            Long managerCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.attachmentId = attachmentId;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long attachmentId() {
        return attachmentId;
    }

    public Long managerCode() {
        return managerCode;
    }

}
