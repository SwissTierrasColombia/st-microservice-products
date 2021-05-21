package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveryStatusEntity;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatus;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryStatusRepository;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.DeliveryStatusJPARepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PostgresDeliveryStatusRepository implements DeliveryStatusRepository {

    private final DeliveryStatusJPARepository repository;

    public PostgresDeliveryStatusRepository(DeliveryStatusJPARepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(DeliveryStatus deliveryStatus) {
        DeliveryStatusEntity deliveryStatusEntity = new DeliveryStatusEntity();
        deliveryStatusEntity.setId(deliveryStatus.id().value());
        deliveryStatusEntity.setName(deliveryStatus.name().value());
        repository.save(deliveryStatusEntity);
    }

    @Override
    public List<DeliveryStatus> all() {
        return repository.findAll().stream().map(deliveryStatusEntity -> DeliveryStatus
                .fromPrimitives(deliveryStatusEntity.getId(), deliveryStatusEntity.getName()))
                .collect(Collectors.toList());
    }

}
