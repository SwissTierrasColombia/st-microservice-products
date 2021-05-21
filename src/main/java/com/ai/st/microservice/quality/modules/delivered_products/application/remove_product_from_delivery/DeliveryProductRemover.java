package com.ai.st.microservice.quality.modules.delivered_products.application.remove_product_from_delivery;

import com.ai.st.microservice.quality.modules.attachments.application.remove_attachment_from_product.AttachmentProductRemover;
import com.ai.st.microservice.quality.modules.attachments.application.remove_attachment_from_product.AttachmentProductRemoverCommand;
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
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

@Service
public final class DeliveryProductRemover implements CommandUseCase<DeliveryProductRemoverCommand> {

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

    @Override
    public void handle(DeliveryProductRemoverCommand command) {

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
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery("No se puede eliminar el producto, porque el estado de la entrega no lo permite.");
        }

    }

    private void removeAttachmentsFromDeliveryProduct(DeliveryId deliveryId, DeliveryProductId deliveryProductId, OperatorCode operatorCode) {
        attachmentRepository.findByDeliveryProductId(deliveryProductId).forEach(attachment ->
                removeAttachment(deliveryId, attachment, operatorCode));
    }

    private void removeAttachment(DeliveryId deliveryId, DeliveryProductAttachment attachment, OperatorCode operatorCode) {
        attachmentProductRemover.handle(new AttachmentProductRemoverCommand(
                deliveryId.value(),
                attachment.deliveryProductId().value(),
                attachment.deliveryProductAttachmentId().value(),
                operatorCode.value()
        ));
    }

}
