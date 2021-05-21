package com.ai.st.microservice.quality.modules.delivered_products.application.create_delivery_product_status;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatus;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatusId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatusName;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductStatusRepository;
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
