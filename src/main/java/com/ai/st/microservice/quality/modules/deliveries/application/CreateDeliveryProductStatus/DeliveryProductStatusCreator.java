package com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryProductStatus;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatus;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusName;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductStatusRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryProductStatusCreator implements CommandUseCase<DeliveryProductStatusCreatorCommand> {

    private final DeliveryProductStatusRepository repository;

    public DeliveryProductStatusCreator(DeliveryProductStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(DeliveryProductStatusCreatorCommand command) {
        DeliveryProductStatus deliveryProductStatus = new DeliveryProductStatus(
                DeliveryProductStatusId.fromValue(command.id()),
                DeliveryProductStatusName.fromValue(command.name()));
        repository.save(deliveryProductStatus);
    }

}
