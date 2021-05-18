package com.ai.st.microservice.quality.modules.deliveries.application.RemoveProductFromDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.RemoveAttachmentFromProduct.AttachmentProductRemover;
import com.ai.st.microservice.quality.modules.deliveries.application.RemoveAttachmentFromProduct.AttachmentProductRemoverCommand;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

@Service
public final class DeliveryProductRemover {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final AttachmentProductRemover attachmentProductRemover;

    public DeliveryProductRemover(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
                                  DeliveryProductAttachmentRepository attachmentRepository, StoreFile storeFile) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.attachmentRepository = attachmentRepository;
        this.attachmentProductRemover = new AttachmentProductRemover(attachmentRepository, deliveryProductRepository, deliveryRepository, storeFile);
    }

    public void remove(DeliveryProductRemoverCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(command.deliveryProductId());
        OperatorCode operatorCode = OperatorCode.fromValue(command.operatorCode());

        verifyPermissions(deliveryId, deliveryProductId, operatorCode);

        removeAttachmentsFromDeliveryProduct(deliveryId, deliveryProductId, operatorCode);

        deliveryProductRepository.remove(deliveryProductId);
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
        if (!delivery.deliveryStatusId().value().equals(DeliveryStatusId.DRAFT)) {
            throw new UnauthorizedToModifyDelivery("No se puede eliminar el producto, porque el estado de la entrega no lo permite.");
        }

    }

    private void removeAttachmentsFromDeliveryProduct(DeliveryId deliveryId, DeliveryProductId deliveryProductId, OperatorCode operatorCode) {
        attachmentRepository.findByDeliveryProductId(deliveryProductId).forEach(attachment ->
                removeAttachment(deliveryId, attachment, operatorCode));
    }

    private void removeAttachment(DeliveryId deliveryId, DeliveryProductAttachment attachment, OperatorCode operatorCode) {
        attachmentProductRemover.remove(new AttachmentProductRemoverCommand(
                deliveryId.value(),
                attachment.deliveryProductId().value(),
                attachment.deliveryProductAttachmentId().value(),
                operatorCode.value()
        ));
    }

}
