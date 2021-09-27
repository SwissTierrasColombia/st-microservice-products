package com.ai.st.microservice.quality.modules.attachments.application.start_quality_process;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentNotFound;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.RemovingAttachmentToProductFailed;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.XTFStatus;
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
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.TaskMicroservice;

import java.util.List;

@Service
public final class QualityProcessStarter implements CommandUseCase<QualityProcessStarterCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final ManagerMicroservice managerMicroservice;
    private final TaskMicroservice taskMicroservice;

    public QualityProcessStarter(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
                                 DeliveryProductAttachmentRepository attachmentRepository, ManagerMicroservice managerMicroservice,
                                 TaskMicroservice taskMicroservice) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.attachmentRepository = attachmentRepository;
        this.managerMicroservice = managerMicroservice;
        this.taskMicroservice = taskMicroservice;
    }

    @Override
    public void handle(QualityProcessStarterCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(command.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = DeliveryProductAttachmentId.fromValue(command.attachmentId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        DeliveryProductXTFAttachment attachment = verifyPermissions(deliveryId, deliveryProductId, attachmentId, managerCode);

        List<UserCode> usersCodeList = managerMicroservice.getUsersByManager(managerCode);

        taskMicroservice.createQualityRulesTask(deliveryId, deliveryProductId, attachment, usersCodeList);

        attachmentRepository.updateXTFStatus(attachment.uuid(), new XTFStatus(XTFStatus.Status.QUALITY_PROCESS_IN_VALIDATION));
    }

    private DeliveryProductXTFAttachment verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
                                                           DeliveryProductAttachmentId attachmentId, ManagerCode managerCode) {

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
            throw new UnauthorizedToModifyDelivery("No se puede iniciar el proceso de calidad porque el estado de la entrega no lo permite.");
        }

        // verify status of the delivery product
        if (!deliveryProduct.isPending()) {
            throw new UnauthorizedToModifyDelivery("No se puede iniciar el proceso de calidad, porque el producto ya fue aceptado o rechazado.");
        }

        // verify type attachment
        if (!deliveryProductAttachment.isXTF()) {
            throw new UnauthorizedToModifyDelivery("No se puede iniciar el proceso de calidad, sólo esta permitido para archivos XTF.");
        }

        // verify status of xtf attachment
        DeliveryProductXTFAttachment attachmentXTF = (DeliveryProductXTFAttachment) deliveryProductAttachment;
        if (!attachmentXTF.accepted()) {
            throw new UnauthorizedToModifyDelivery("El archivo ya se encuentra en el proceso de calidad.");
        }

        // verify attachment belong to delivery product
        if (!deliveryProductAttachment.deliveryProductId().value().equals(deliveryProductId.value())) {
            throw new RemovingAttachmentToProductFailed("El adjunto no pertenece al producto.");
        }

        return attachmentXTF;
    }

}
