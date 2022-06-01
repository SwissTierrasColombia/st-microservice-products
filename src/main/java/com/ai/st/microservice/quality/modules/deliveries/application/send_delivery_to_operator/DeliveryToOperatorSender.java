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
import com.ai.st.microservice.quality.modules.feedbacks.domain.contracts.DeliveryProductFeedbackRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class DeliveryToOperatorSender implements CommandUseCase<DeliveryToOperatorSenderCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductFeedbackRepository feedbackRepository;

    public DeliveryToOperatorSender(DeliveryRepository deliveryRepository,
            DeliveryProductRepository deliveryProductRepository, DeliveryProductFeedbackRepository feedbackRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public void handle(DeliveryToOperatorSenderCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.getDeliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.getManagerCode());

        verifyPermissions(deliveryId, managerCode);

        deliveryRepository.changeStatus(deliveryId, DeliveryStatusId.fromValue(DeliveryStatusId.IN_REMEDIATION), null);
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
            throw new UnauthorizedToModifyDelivery(
                    "No se puede enviar la entrega, porque el estado de la misma no lo permite.");
        }

        List<DeliveryProduct> deliveryProducts = deliveryProductRepository.findByDeliveryId(deliveryId);

        verifyIfThereAreNoUncheckedProducts(deliveryProducts);
        verifyIfAllProductsAreAccepted(deliveryProducts);
        verifyFeedbacksForProductsRejected(deliveryProducts);
    }

    private void verifyIfThereAreNoUncheckedProducts(List<DeliveryProduct> deliveryProducts) {
        long count = deliveryProducts.stream().filter(DeliveryProduct::isPending).count();
        if (count > 0)
            throw new UnauthorizedToModifyDelivery(
                    "No se puede enviar la entrega, porque aún hay productos sin revisar.");
    }

    private void verifyIfAllProductsAreAccepted(List<DeliveryProduct> deliveryProducts) {
        long countTotal = deliveryProducts.size();
        long countAccepted = deliveryProducts.stream().filter(DeliveryProduct::isAccepted).count();

        if (countTotal == countAccepted)
            throw new UnauthorizedToModifyDelivery(
                    "No se puede enviar la entrega, porque todos los productos fueron aceptados.");
    }

    private void verifyFeedbacksForProductsRejected(List<DeliveryProduct> deliveryProducts) {
        long countProductsWrong = deliveryProducts.stream().filter(DeliveryProduct::isRejected)
                .collect(Collectors.toList()).stream().filter(deliveryProduct -> feedbackRepository
                        .findByDeliveryProductId(deliveryProduct.deliveryProductId()).size() == 0)
                .count();

        if (countProductsWrong > 0)
            throw new UnauthorizedToModifyDelivery(
                    "Existen productos rechazados a los cuales no se les ha creado un feedback.");
    }

}
