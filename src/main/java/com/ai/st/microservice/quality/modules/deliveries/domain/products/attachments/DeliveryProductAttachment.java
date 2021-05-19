package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;

public abstract class DeliveryProductAttachment {

    private final DeliveryProductAttachmentId deliveryProductAttachmentId;
    private final DeliveryProductAttachmentUUID uuid;
    private final DeliveryProductAttachmentObservations observations;
    private final DeliveryProductId deliveryProductId;
    private final DeliveryProductAttachmentDate deliveryProductAttachmentDate;

    public DeliveryProductAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentUUID uuid,
                                     DeliveryProductAttachmentObservations observations,
                                     DeliveryProductId deliveryProductId, DeliveryProductAttachmentDate deliveryProductAttachmentDate) {
        this.deliveryProductAttachmentId = id;
        this.uuid = uuid;
        this.observations = observations;
        this.deliveryProductId = deliveryProductId;
        this.deliveryProductAttachmentDate = deliveryProductAttachmentDate;
    }

    public DeliveryProductAttachmentId deliveryProductAttachmentId() {
        return deliveryProductAttachmentId;
    }

    public DeliveryProductAttachmentObservations observations() {
        return observations;
    }

    public DeliveryProductId deliveryProductId() {
        return deliveryProductId;
    }

    public DeliveryProductAttachmentDate deliveryProductAttachmentDate() {
        return deliveryProductAttachmentDate;
    }

    public DeliveryProductAttachmentUUID uuid() {
        return uuid;
    }

    public boolean isXTF() {
        return this instanceof DeliveryProductXTFAttachment;
    }

}
