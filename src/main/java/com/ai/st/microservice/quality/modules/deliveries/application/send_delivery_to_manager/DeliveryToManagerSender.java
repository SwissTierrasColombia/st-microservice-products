package com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_manager;

import com.ai.st.microservice.quality.modules.deliveries.application.verify_integrity_delivery.IntegrityDeliveryChecker;
import com.ai.st.microservice.quality.modules.deliveries.application.verify_integrity_delivery.IntegrityDeliveryCheckerCommand;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryToManagerSender implements CommandUseCase<DeliveryToManagerSenderCommand> {

    private final DeliveryRepository deliveryRepository;
    private final IntegrityDeliveryChecker integrityDeliveryChecker;

    public DeliveryToManagerSender(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
                                   ProductRepository productRepository, DeliveryProductAttachmentRepository attachmentRepository) {
        this.deliveryRepository = deliveryRepository;
        this.integrityDeliveryChecker = new IntegrityDeliveryChecker(deliveryProductRepository, attachmentRepository, productRepository);
    }

    @Override
    public void handle(DeliveryToManagerSenderCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        OperatorCode operatorCode = OperatorCode.fromValue(command.operatorCode());

        verifyPermissions(deliveryId, operatorCode);

        deliveryRepository.changeStatus(deliveryId, DeliveryStatusId.fromValue(DeliveryStatusId.DELIVERED));
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
            throw new UnauthorizedToModifyDelivery("No se puede enviar la entrega, porque el estado de la misma no lo permite.");
        }

        integrityDeliveryChecker.handle(new IntegrityDeliveryCheckerCommand(deliveryId.value()));
    }


}
