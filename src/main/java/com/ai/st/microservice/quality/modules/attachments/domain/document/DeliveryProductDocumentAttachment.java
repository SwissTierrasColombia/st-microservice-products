package com.ai.st.microservice.quality.modules.attachments.domain.document;

import com.ai.st.microservice.quality.modules.attachments.domain.*;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;

import java.util.Date;

public final class DeliveryProductDocumentAttachment extends DeliveryProductAttachment {

    private final DocumentUrl documentUrl;

    public DeliveryProductDocumentAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentUUID uuid,
            DeliveryProductAttachmentObservations observations, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentDate deliveryProductAttachmentDate, DocumentUrl documentUrl) {
        super(id, uuid, observations, deliveryProductId, deliveryProductAttachmentDate);
        this.documentUrl = documentUrl;
    }

    public static DeliveryProductDocumentAttachment create(DeliveryProductAttachmentUUID uuid,
            DeliveryProductAttachmentObservations observations, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentDate deliveryProductAttachmentDate, DocumentUrl documentUrl) {
        return new DeliveryProductDocumentAttachment(null, uuid, observations, deliveryProductId,
                deliveryProductAttachmentDate, documentUrl);
    }

    public static DeliveryProductDocumentAttachment fromPrimitives(Long id, String uuid, String observations,
            Long deliveryProductId, Date date, String url) {
        return new DeliveryProductDocumentAttachment(new DeliveryProductAttachmentId(id),
                new DeliveryProductAttachmentUUID(uuid), new DeliveryProductAttachmentObservations(observations),
                new DeliveryProductId(deliveryProductId), new DeliveryProductAttachmentDate(date),
                new DocumentUrl(url));
    }

    public DocumentUrl documentUrl() {
        return documentUrl;
    }

}
