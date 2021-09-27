package com.ai.st.microservice.quality.modules.attachments.application.find_attachment_from_product;

import com.ai.st.microservice.quality.modules.shared.application.Query;

public final class AttachmentFinderQuery implements Query {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long attachmentId;
    private final boolean onlyXTF;

    public AttachmentFinderQuery(Long deliveryId, Long deliveryProductId, Long attachmentId, boolean onlyXTF) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.attachmentId = attachmentId;
        this.onlyXTF = onlyXTF;
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

    public boolean isOnlyXTF() {
        return onlyXTF;
    }

}
