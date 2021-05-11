package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveryEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveryJPARepository extends CrudRepository<DeliveryEntity, Long> {
}
