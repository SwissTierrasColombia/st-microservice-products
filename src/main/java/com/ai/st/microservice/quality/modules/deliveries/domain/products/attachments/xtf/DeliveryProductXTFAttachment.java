package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentObservations;

public final class DeliveryProductXTFAttachment extends DeliveryProductAttachment {

    private final XTFValid isValid;
    private final XTFUrl url;
    private final XTFVersion version;

    public DeliveryProductXTFAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentObservations observations,
                                        XTFValid valid, XTFUrl url, XTFVersion version) {
        super(id, observations);
        this.isValid = valid;
        this.url = url;
        this.version = version;
    }

    public XTFValid valid() {
        return isValid;
    }

    public XTFUrl url() {
        return url;
    }

    public XTFVersion version() {
        return version;
    }
}
