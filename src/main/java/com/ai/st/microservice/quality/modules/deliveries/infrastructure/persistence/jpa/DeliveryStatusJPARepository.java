package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveryStatusEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryStatusJPARepository extends CrudRepository<DeliveryStatusEntity, Long> {

    @Override
    List<DeliveryStatusEntity> findAll();

}
