package com.ai.st.microservice.quality.modules.deliveries.application.start_review;

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
public final class ReviewStarter implements CommandUseCase<ReviewStarterCommand> {

    private final DeliveryRepository deliveryRepository;

    public ReviewStarter(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void handle(ReviewStarterCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        verifyPermissions(deliveryId, managerCode);

        deliveryRepository.changeState(deliveryId, DeliveryStatusId.fromValue(DeliveryStatusId.IN_REVIEW));
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
        if (!delivery.isDelivered()) {
            throw new UnauthorizedToModifyDelivery("No se puede iniciar la revisión, porque el estado de la misma no lo permite.");
        }
    }

}
