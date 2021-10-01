package com.ai.st.microservice.quality.modules.attachments.application.update_xtf_status;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.XTFStatus;
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

        DeliveryProductAttachmentUUID uuid;
        if (command.attachmentUUID() != null) {
            uuid = new DeliveryProductAttachmentUUID(command.attachmentUUID());
        } else {
            DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(DeliveryProductAttachmentId.fromValue(command.attachmentId()));
            uuid = new DeliveryProductAttachmentUUID(deliveryProductAttachment.uuid().value());
        }

        XTFStatus status;
        if (command.status().equals(XTFStatusUpdaterCommand.Status.ACCEPTED)) {
            status = new XTFStatus(XTFStatus.Status.ACCEPTED);
            this.removeReportRevision(uuid);
        } else if (command.status().equals(XTFStatusUpdaterCommand.Status.REJECTED)) {
            status = new XTFStatus(XTFStatus.Status.REJECTED);
        } else {
            status = new XTFStatus(XTFStatus.Status.QUALITY_PROCESS_FINISHED);
        }

        attachmentRepository.updateXTFStatus(uuid, status);
    }

    private void removeReportRevision(DeliveryProductAttachmentUUID uuid) {
        this.attachmentRepository.updateReportRevisionXTF(uuid, null, null);
    }

}
