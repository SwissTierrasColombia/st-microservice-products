package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveredProductAttachmentJPARepository extends CrudRepository<DeliveredProductAttachmentEntity, Long> {

    DeliveredProductAttachmentEntity findByUuid(String uuid);

    List<DeliveredProductAttachmentEntity> findByDeliveredProduct(DeliveredProductEntity deliveredProduct);

}
