package com.ai.st.microservice.quality.modules.attachments.application.remove_attachment_from_product;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

@Service
public final class AttachmentProductRemover implements CommandUseCase<AttachmentProductRemoverCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final StoreFile storeFile;

    public AttachmentProductRemover(DeliveryProductAttachmentRepository attachmentRepository, DeliveryProductRepository deliveryProductRepository,
                                    DeliveryRepository deliveryRepository, StoreFile storeFile) {
        this.storeFile = storeFile;
        this.attachmentRepository = attachmentRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    @Override
    public void handle(AttachmentProductRemoverCommand command) {

        DeliveryId deliveryId = new DeliveryId(command.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(command.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = new DeliveryProductAttachmentId(command.attachmentId());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());

        verifyPermissions(deliveryId, deliveryProductId, operatorCode);

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);

        if (deliveryProductAttachment != null) {
            deleteStorage(deliveryProductAttachment);
            attachmentRepository.remove(attachmentId);
        }

    }

    private void verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId, OperatorCode operatorCode) {

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

        // verify owner of the delivery
        if (!delivery.deliveryBelongToOperator(operatorCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery("No se puede eliminar el adjunto, porque el estado de la entrega no lo permite.");
        }

    }

    private void deleteStorage(DeliveryProductAttachment deliveryProductAttachment) {

        String pathFile = null;

        if (deliveryProductAttachment instanceof DeliveryProductXTFAttachment) {
            DeliveryProductXTFAttachment xtf = (DeliveryProductXTFAttachment) deliveryProductAttachment;
            pathFile = xtf.url().value();
        }

        if (deliveryProductAttachment instanceof DeliveryProductDocumentAttachment) {
            DeliveryProductDocumentAttachment document = (DeliveryProductDocumentAttachment) deliveryProductAttachment;
            pathFile = document.documentUrl().value();
        }

        storeFile.deleteFile(pathFile);
    }

}
