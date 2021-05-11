package com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryProductStatus;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatus;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusName;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductStatusRepository;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryProductStatusCreator {

    private final DeliveryProductStatusRepository repository;

    public DeliveryProductStatusCreator(DeliveryProductStatusRepository repository) {
        this.repository = repository;
    }

    public void create(Long id, String name) {
        DeliveryProductStatus deliveryProductStatus = new DeliveryProductStatus(new DeliveryProductStatusId(id), new DeliveryProductStatusName(name));
        repository.save(deliveryProductStatus);
    }
}
