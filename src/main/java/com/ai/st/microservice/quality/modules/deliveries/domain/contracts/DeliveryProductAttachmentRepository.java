package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.XTFStatus;

public interface DeliveryProductAttachmentRepository {

    void save(DeliveryProductAttachment deliveryProductAttachment);

    DeliveryProductAttachment search(DeliveryProductAttachmentId id);

    DeliveryProductAttachment search(DeliveryProductAttachmentUUID uuid);

    void updateXTFStatus(DeliveryProductAttachmentUUID uuid, XTFStatus status);

}
