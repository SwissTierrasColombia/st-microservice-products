package com.ai.st.microservice.quality.modules.deliveries.domain.contracts;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.XTFStatus;

import java.util.List;

public interface DeliveryProductAttachmentRepository {

    DeliveryProductAttachment search(DeliveryProductAttachmentId id);

    DeliveryProductAttachment search(DeliveryProductAttachmentUUID uuid);

    List<DeliveryProductAttachment> findByDeliveryProductId(DeliveryProductId deliveryProductId);

    void save(DeliveryProductAttachment deliveryProductAttachment);

    void updateXTFStatus(DeliveryProductAttachmentUUID uuid, XTFStatus status);

    void remove(DeliveryProductAttachmentId id);

}
