package com.ai.st.microservice.quality.modules.deliveries.application.create_delivery_status;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusName;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryStatusRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryStatusCreator implements CommandUseCase<DeliveryStatusCreatorCommand> {

    private final DeliveryStatusRepository repository;

    public DeliveryStatusCreator(DeliveryStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(DeliveryStatusCreatorCommand command) {
        DeliveryStatus deliveryStatus = new DeliveryStatus(
                DeliveryStatusId.fromValue(command.id()),
                DeliveryStatusName.fromValue(command.name()));
        repository.save(deliveryStatus);
    }

}
