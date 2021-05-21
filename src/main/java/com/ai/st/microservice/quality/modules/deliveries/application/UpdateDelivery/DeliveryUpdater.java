package com.ai.st.microservice.quality.modules.deliveries.application.UpdateDelivery;

import com.ai.st.microservice.quality.modules.deliveries.domain.*;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.*;

@Service
public final class DeliveryUpdater implements CommandUseCase<DeliveryUpdaterCommand> {

    private final DeliveryRepository deliveryRepository;

    public DeliveryUpdater(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void handle(DeliveryUpdaterCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        OperatorCode operatorCode = OperatorCode.fromValue(command.operatorCode());
        DeliveryObservations observations = DeliveryObservations.fromValue(command.observations());

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
            throw new UnauthorizedToModifyDelivery("No se puede actualizar la entrega, porque el estado de la entrega no lo permite.");
        }

        deliveryRepository.update(Delivery.create(
                deliveryId,
                delivery.code(),
                delivery.municipality(),
                delivery.manager(),
                delivery.operator(),
                delivery.user(),
                observations,
                delivery.deliveryDate(),
                delivery.deliveryStatusId()
        ));
    }

}
