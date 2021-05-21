package com.ai.st.microservice.quality.modules.deliveries.application.UpdateXTFStatus;

import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.XTFStatus;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class XTFStatusUpdater implements CommandUseCase<XTFStatusUpdaterCommand> {

    private final DeliveryProductAttachmentRepository attachmentRepository;

    public XTFStatusUpdater(DeliveryProductAttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public void handle(XTFStatusUpdaterCommand command) {

        DeliveryProductAttachmentUUID uuid = new DeliveryProductAttachmentUUID(command.attachmentUUID());

        XTFStatus status;
        if (command.status().equals(XTFStatusUpdaterCommand.Status.ACCEPTED)) {
            status = new XTFStatus(XTFStatus.Status.ACCEPTED);
        } else {
            status = new XTFStatus(XTFStatus.Status.REJECTED);
        }

        attachmentRepository.updateXTFStatus(uuid, status);
    }

}
