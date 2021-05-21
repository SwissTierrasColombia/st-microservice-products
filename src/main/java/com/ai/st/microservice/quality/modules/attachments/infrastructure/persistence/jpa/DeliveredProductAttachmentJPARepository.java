package com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveredProductAttachmentJPARepository extends CrudRepository<DeliveredProductAttachmentEntity, Long> {

    DeliveredProductAttachmentEntity findByUuid(String uuid);

    List<DeliveredProductAttachmentEntity> findByDeliveredProduct(DeliveredProductEntity deliveredProduct);

}
