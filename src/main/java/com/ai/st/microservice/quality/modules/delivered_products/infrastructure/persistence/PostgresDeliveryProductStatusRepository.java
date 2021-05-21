package com.ai.st.microservice.quality.modules.delivered_products.infrastructure.persistence;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveryProductStatusEntity;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatus;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductStatusRepository;
import com.ai.st.microservice.quality.modules.delivered_products.infrastructure.persistence.jpa.DeliveryProductStatusJPARepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PostgresDeliveryProductStatusRepository implements DeliveryProductStatusRepository {

    private final DeliveryProductStatusJPARepository repository;

    public PostgresDeliveryProductStatusRepository(DeliveryProductStatusJPARepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(DeliveryProductStatus deliveryProductStatus) {
        DeliveryProductStatusEntity statusEntity = new DeliveryProductStatusEntity();
        statusEntity.setId(deliveryProductStatus.id().value());
        statusEntity.setName(deliveryProductStatus.name().value());
        repository.save(statusEntity);
    }

    @Override
    public List<DeliveryProductStatus> all() {
        return repository.findAll().stream().map(deliveryProductStatusEntity ->
                DeliveryProductStatus.fromPrimitives(deliveryProductStatusEntity.getId(), deliveryProductStatusEntity.getName()))
                .collect(Collectors.toList());
    }
}
