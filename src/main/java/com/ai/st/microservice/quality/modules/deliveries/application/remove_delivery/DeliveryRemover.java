package com.ai.st.microservice.quality.modules.deliveries.application.remove_delivery;

import com.ai.st.microservice.quality.modules.delivered_products.application.remove_product_from_delivery.DeliveryProductRemover;
import com.ai.st.microservice.quality.modules.delivered_products.application.remove_product_from_delivery.DeliveryProductRemoverCommand;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

@Service
public final class DeliveryRemover implements CommandUseCase<DeliveryRemoverCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductRemover deliveryProductRemover;

    public DeliveryRemover(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
            DeliveryProductAttachmentRepository attachmentRepository, StoreFile storeFile) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.deliveryProductRemover = new DeliveryProductRemover(deliveryRepository, deliveryProductRepository,
                attachmentRepository, storeFile);
    }

    @Override
    public void handle(DeliveryRemoverCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        OperatorCode operatorCode = OperatorCode.fromValue(command.operatorCode());

        verifyPermissions(deliveryId, operatorCode);

        removeProductsFromDelivery(deliveryId, operatorCode);

        deliveryRepository.remove(deliveryId);
    }

    private void verifyPermissions(DeliveryId deliveryId, OperatorCode operatorCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToOperator(operatorCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery(
                    "No se puede eliminar la entrega, porque el estado de la entrega no lo permite.");
        }

    }

    private void removeProductsFromDelivery(DeliveryId deliveryId, OperatorCode operatorCode) {
        deliveryProductRepository.findByDeliveryId(deliveryId)
                .forEach(deliveryProduct -> removeProductFromDelivery(deliveryId, deliveryProduct, operatorCode));
    }

    private void removeProductFromDelivery(DeliveryId deliveryId, DeliveryProduct deliveryProduct,
            OperatorCode operatorCode) {
        deliveryProductRemover.handle(new DeliveryProductRemoverCommand(deliveryId.value(),
                deliveryProduct.deliveryProductId().value(), operatorCode.value()));
    }

}
