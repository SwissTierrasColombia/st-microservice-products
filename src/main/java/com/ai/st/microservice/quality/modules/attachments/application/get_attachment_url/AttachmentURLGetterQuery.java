package com.ai.st.microservice.quality.modules.attachments.application.get_attachment_url;

import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.shared.application.Query;

public final class AttachmentURLGetterQuery implements Query {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long attachmentId;
    private final Roles role;
    private final Long entityCode;

    public AttachmentURLGetterQuery(Long deliveryId, Long deliveryProductId, Long attachmentId, Roles role, Long entityCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.attachmentId = attachmentId;
        this.role = role;
        this.entityCode = entityCode;
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

    public Roles role() {
        return role;
    }

    public Long entityCode() {
        return entityCode;
    }

}
