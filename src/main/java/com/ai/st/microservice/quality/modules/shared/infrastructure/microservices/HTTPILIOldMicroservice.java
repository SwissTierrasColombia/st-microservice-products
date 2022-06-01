package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.IliOldFeignClient;
import com.ai.st.microservice.common.dto.ili.MicroserviceIlivalidatorBackgroundDto;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.ILIOldMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.MicroserviceUnreachable;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class HTTPILIOldMicroservice implements ILIOldMicroservice {

    private final Logger log = LoggerFactory.getLogger(HTTPILIOldMicroservice.class);

    private static final String MODEL_VERSION = "1.0";
    private static final Long CONCEPT_ID = (long) 3;
    private static final String QUEUE_RESPONSE = "QUEUE_UPDATE_STATE_XTF_PRODUCTS";

    private final IliOldFeignClient iliFeignClient;

    public HTTPILIOldMicroservice(IliOldFeignClient iliFeignClient) {
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
            String messageError = String.format("Error enviando a validación el adjunto XTF %s con version 1.0: %s",
                    attachmentUUID.value(), e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new MicroserviceUnreachable("ili");
        }

    }

}
