package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentJPARepository extends CrudRepository<DeliveredProductAttachmentEntity, Long> {

    DeliveredProductAttachmentEntity findByUuid(String uuid);

}
