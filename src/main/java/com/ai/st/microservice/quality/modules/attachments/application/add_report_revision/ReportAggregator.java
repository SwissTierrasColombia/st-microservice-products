package com.ai.st.microservice.quality.modules.attachments.application.add_report_revision;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentNotFound;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.XTFReportRevisionUrl;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.TaskXTFQualityControl;
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.TaskMicroservice;

@Service
public final class ReportAggregator implements CommandUseCase<ReportAggregatorCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final TaskMicroservice taskMicroservice;
    private final StoreFile storeFile;

    public ReportAggregator(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
                            DeliveryProductAttachmentRepository attachmentRepository, TaskMicroservice taskMicroservice,
                            StoreFile storeFile) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.attachmentRepository = attachmentRepository;
        this.taskMicroservice = taskMicroservice;
        this.storeFile = storeFile;
    }

    @Override
    public void handle(ReportAggregatorCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(command.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = DeliveryProductAttachmentId.fromValue(command.attachmentId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());
        UserCode userCode = new UserCode(command.userCode());

        DeliveryProductXTFAttachment attachment = verifyPermissions(deliveryId, deliveryProductId, attachmentId,
                managerCode, userCode, command.overwrite());

        String namespace = buildNamespace(deliveryId);
        String pathUrl = storeFile.storeFilePermanently(command.bytesFile(), command.extensionFile(), namespace);

        attachmentRepository.updateReportRevisionXTF(attachment.uuid(), new XTFReportRevisionUrl(pathUrl, false));
    }

    private DeliveryProductXTFAttachment verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
                                                           DeliveryProductAttachmentId attachmentId, ManagerCode managerCode,
                                                           UserCode userCode, boolean overwriteReport) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify delivery product exists
        DeliveryProduct deliveryProduct = deliveryProductRepository.search(deliveryProductId);
        if (deliveryProduct == null) {
            throw new DeliveryProductNotFound();
        }

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);
        if (deliveryProductAttachment == null) {
            throw new AttachmentNotFound();
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isInReview()) {
            throw new UnauthorizedToModifyDelivery("No se puede cargar el reporte de revisión porque el estado de la entrega no lo permite.");
        }

        // verify status of the delivery product
        if (!deliveryProduct.isPending()) {
            throw new UnauthorizedToModifyDelivery("No se puede cargar el reporte de revisión porque el producto ya fue aceptado o rechazado.");
        }

        // verify type attachment
        if (!deliveryProductAttachment.isXTF()) {
            throw new UnauthorizedToModifyDelivery("No se puede cargar el reporte de revisión porque el archivo no es un XTF.");
        }

        // verify status of xtf attachment
        DeliveryProductXTFAttachment attachmentXTF = (DeliveryProductXTFAttachment) deliveryProductAttachment;
        if (!attachmentXTF.qualityInValidation()) {
            throw new UnauthorizedToModifyDelivery("No se puede cargar el reporte de revisión porque el archivo no está en estado de validación.");
        }

        // verify if file has already a report
        if (attachmentXTF.hasReportRevisionURL() && !overwriteReport) {
            throw new UnauthorizedToModifyDelivery("El archivo XTF ya tiene cargado un reporte");
        }
        if (attachmentXTF.hasReportRevisionURL()) {
            storeFile.deleteFile(attachmentXTF.url().value());
        }

        // verify if exits any open tasks related to the attachment
        TaskXTFQualityControl task = taskMicroservice.findQualityProcessTask(attachmentXTF);
        if (task == null) {
            throw new UnauthorizedToModifyDelivery("No se puede cargar el reporte de revisión porque no existe una tarea abierta para el proceso de calidad.");
        }

        // verify user belongs to task
        if (!task.userId().equals(userCode.value())) {
            throw new UnauthorizedToModifyDelivery("El usuario no pertenece a la tarea por lo cual no puede cargar el reporte de revisión.");
        }

        // verify attachment belong to delivery product
        if (!deliveryProductAttachment.deliveryProductId().value().equals(deliveryProductId.value())) {
            throw new UnauthorizedToModifyDelivery("El adjunto no pertenece al producto.");
        }

        return attachmentXTF;
    }

    private String buildNamespace(DeliveryId deliveryId) {
        return String.format("/entregas/%d", deliveryId.value());
    }

}
