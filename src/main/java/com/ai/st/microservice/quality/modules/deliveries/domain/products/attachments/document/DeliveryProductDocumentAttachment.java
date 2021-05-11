package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentObservations;

public final class DeliveryProductDocumentAttachment extends DeliveryProductAttachment {

    private final DocumentUrl documentUrl;

    public DeliveryProductDocumentAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentObservations observations,
                                             DocumentUrl documentUrl) {
        super(id, observations);
        this.documentUrl = documentUrl;
    }

    public DocumentUrl documentUrl() {
        return documentUrl;
    }

}
