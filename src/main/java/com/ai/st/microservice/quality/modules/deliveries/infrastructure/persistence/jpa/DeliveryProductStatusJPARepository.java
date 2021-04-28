package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.infrastructure.persistence.entities.DeliveryProductStatusEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryProductStatusJPARepository extends CrudRepository<DeliveryProductStatusEntity, Long> {

    @Override
    List<DeliveryProductStatusEntity> findAll();

}
