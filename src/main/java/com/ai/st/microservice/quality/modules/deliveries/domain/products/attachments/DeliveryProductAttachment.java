package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments;

public abstract class DeliveryProductAttachment {

    private final DeliveryProductAttachmentId id;
    private final DeliveryProductAttachmentObservations observations;

    public DeliveryProductAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentObservations observations) {
        this.id = id;
        this.observations = observations;
    }

    public DeliveryProductAttachmentId id() {
        return id;
    }

    public DeliveryProductAttachmentObservations observations() {
        return observations;
    }

}
