package com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_operator;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryToOperatorSender implements CommandUseCase<DeliveryToOperatorSenderCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;

    public DeliveryToOperatorSender(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    @Override
    public void handle(DeliveryToOperatorSenderCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.getDeliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.getManagerCode());

        verifyPermissions(deliveryId, managerCode);

        deliveryRepository.changeStatus(deliveryId, DeliveryStatusId.fromValue(DeliveryStatusId.IN_REMEDIATION));
    }

    private void verifyPermissions(DeliveryId deliveryId, ManagerCode managerCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isInReview()) {
            throw new UnauthorizedToModifyDelivery("No se puede enviar la entrega, porque el estado de la misma no lo permite.");
        }

        verifyThereAreNoUncheckedProducts(deliveryId);
    }

    private void verifyThereAreNoUncheckedProducts(DeliveryId deliveryId) {
        long count = deliveryProductRepository.findByDeliveryId(deliveryId).stream().filter(DeliveryProduct::isPending).count();
        if (count > 0)
            throw new UnauthorizedToModifyDelivery("No se puede enviar la entrega, porque aún hay productos sin revisar.");
    }

}
