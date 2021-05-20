package com.ai.st.microservice.quality.entrypoints.events.deliveries;

import com.ai.st.microservice.common.dto.ili.MicroserviceValidationDto;
import com.ai.st.microservice.quality.modules.deliveries.application.UpdateXTFStatus.XTFStatusUpdater;
import com.ai.st.microservice.quality.modules.deliveries.application.UpdateXTFStatus.XTFStatusUpdaterCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public final class UpdateStateXTFOnValidationDone {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final XTFStatusUpdater xtfStatusUpdater;

    public UpdateStateXTFOnValidationDone(XTFStatusUpdater xtfStatusUpdater) {
        this.xtfStatusUpdater = xtfStatusUpdater;
    }

    @RabbitListener(queues = "${st.rabbitmq.queueResultValidationProducts.queue}", concurrency = "${st.rabbitmq.queueResultValidationProducts.concurrency}")
    public void resultValidation(MicroserviceValidationDto validationDto) {


        try {

            log.info("Updating state xtf with result: " + validationDto.getIsValid());

            String referenceId = validationDto.getReferenceId();

            if (referenceId != null && !referenceId.isEmpty()) {

                XTFStatusUpdaterCommand.Status status = (validationDto.getIsValid())
                        ? XTFStatusUpdaterCommand.Status.ACCEPTED : XTFStatusUpdaterCommand.Status.REJECTED;

                xtfStatusUpdater.update(new XTFStatusUpdaterCommand(
                        status,
                        validationDto.getReferenceId()
                ));
            }

        } catch (Exception e) {
            log.error("Error updating state xtf: " + e.getMessage());
        }


    }

}
