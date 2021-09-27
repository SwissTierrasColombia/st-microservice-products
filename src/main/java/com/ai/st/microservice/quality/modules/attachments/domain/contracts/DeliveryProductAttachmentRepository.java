package com.ai.st.microservice.quality.modules.attachments.domain.contracts;

import com.ai.st.microservice.quality.modules.attachments.domain.xtf.XTFReportRevisionUrl;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.XTFStatus;

import java.util.List;

public interface DeliveryProductAttachmentRepository {

    DeliveryProductAttachment search(DeliveryProductAttachmentId id);

    DeliveryProductAttachment search(DeliveryProductAttachmentUUID uuid);

    List<DeliveryProductAttachment> findByDeliveryProductId(DeliveryProductId deliveryProductId);

    void save(DeliveryProductAttachment deliveryProductAttachment);

    void updateXTFStatus(DeliveryProductAttachmentUUID uuid, XTFStatus status);

    void remove(DeliveryProductAttachmentId id);

    void updateReportRevisionXTF(DeliveryProductAttachmentUUID uuid, XTFReportRevisionUrl reportRevisionUrl);

}
