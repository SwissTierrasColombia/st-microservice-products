package com.ai.st.microservice.quality.modules.shared.domain.contracts;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentUUID;

public interface ILIOldMicroservice {

    void sendToValidation(DeliveryProductAttachmentUUID attachmentUUID, String pathFile, boolean skipGeometryValidation,
            boolean skipErrors);

}
