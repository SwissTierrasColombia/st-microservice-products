package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.IliFeignClient;
import com.ai.st.microservice.common.dto.ili.MicroserviceIlivalidatorBackgroundDto;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.MicroserviceUnreachable;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.ILIMicroservice;
import org.springframework.stereotype.Service;

@Service
public final class HTTPILIMicroservice implements ILIMicroservice {

    private static final String MODEL_VERSION = "1.0";
    private static final Long CONCEPT_ID = (long) 3;
    private static final String QUEUE_RESPONSE = "QUEUE_UPDATE_STATE_XTF_PRODUCTS";

    private final IliFeignClient iliFeignClient;

    public HTTPILIMicroservice(IliFeignClient iliFeignClient) {
        this.iliFeignClient = iliFeignClient;
    }

    @Override
    public void sendToValidation(DeliveryProductAttachmentUUID attachmentUUID, String pathFile,
                                 boolean skipGeometryValidation, boolean skipErrors) {

        try {

            MicroserviceIlivalidatorBackgroundDto request = new MicroserviceIlivalidatorBackgroundDto();
            request.setConceptId(CONCEPT_ID);
            request.setVersionModel(MODEL_VERSION);
            request.setQueueResponse(QUEUE_RESPONSE);
            request.setPathFile(pathFile);
            request.setSkipErrors(skipErrors);
            request.setReferenceId(attachmentUUID.value());
            request.setSkipGeometryValidation(skipGeometryValidation);

            iliFeignClient.startValidation(request);

        } catch (Exception e) {
            throw new MicroserviceUnreachable("ili");
        }

    }

}
