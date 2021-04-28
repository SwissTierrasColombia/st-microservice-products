package com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryStatus;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusName;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryStatusRepository;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryStatusCreator {

    private final DeliveryStatusRepository repository;

    public DeliveryStatusCreator(DeliveryStatusRepository repository) {
        this.repository = repository;
    }

    public void create(Long id, String name) {

        DeliveryStatus deliveryStatus = new DeliveryStatus(new DeliveryStatusId(id), new DeliveryStatusName(name));

        repository.save(deliveryStatus);

    }


}
