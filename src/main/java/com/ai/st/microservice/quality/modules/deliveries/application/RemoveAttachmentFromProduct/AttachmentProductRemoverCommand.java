package com.ai.st.microservice.quality.modules.deliveries.application.RemoveAttachmentFromProduct;

public final class AttachmentProductRemoverCommand {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long attachmentId;
    private final Long operatorCode;

    public AttachmentProductRemoverCommand(Long deliveryId, Long deliveryProductId, Long attachmentId, Long operatorId) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.attachmentId = attachmentId;
        this.operatorCode = operatorId;
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

    public Long operatorCode() {
        return operatorCode;
    }
}
