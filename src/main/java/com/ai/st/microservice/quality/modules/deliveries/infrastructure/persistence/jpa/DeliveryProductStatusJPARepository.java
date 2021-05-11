package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveryProductStatusEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryProductStatusJPARepository extends CrudRepository<DeliveryProductStatusEntity, Long> {

    @Override
    List<DeliveryProductStatusEntity> findAll();

}
