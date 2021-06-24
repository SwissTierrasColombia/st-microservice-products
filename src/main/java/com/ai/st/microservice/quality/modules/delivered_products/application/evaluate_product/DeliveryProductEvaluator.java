package com.ai.st.microservice.quality.modules.delivered_products.application.evaluate_product;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatusId;
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

@Service
public final class DeliveryProductEvaluator implements CommandUseCase<DeliveryProductEvaluatorCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;

    public DeliveryProductEvaluator(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    @Override
    public void handle(DeliveryProductEvaluatorCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(command.deliveryProductId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        verifyPermissions(deliveryId, deliveryProductId, managerCode);

        deliveryProductRepository.changeStatus(deliveryProductId, mappingStatus(command.state()));
    }

    private void verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId, ManagerCode managerCode) {

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
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isInReview()) {
            throw new UnauthorizedToModifyDelivery("No se puede aceptar o rechazar el producto, porque el estado de la entrega no lo permite.");
        }

        // verify status of the product
        if (!deliveryProduct.isPending()) {
            throw new UnauthorizedToModifyDelivery("No se puede aceptar o rechazar el producto, porque el estado del producto no lo permite.");
        }

    }

    private DeliveryProductStatusId mappingStatus(DeliveryProductEvaluatorCommand.Statuses status) {
        if (status.equals(DeliveryProductEvaluatorCommand.Statuses.ACCEPTED)) {
            return DeliveryProductStatusId.fromValue(DeliveryProductStatusId.ACCEPTED);
        }
        return DeliveryProductStatusId.fromValue(DeliveryProductStatusId.REJECTED);
    }

}
