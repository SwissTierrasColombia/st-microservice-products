package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentFTPEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentFTPJPARepository extends CrudRepository<DeliveredProductAttachmentFTPEntity, Long> {

    DeliveredProductAttachmentFTPEntity findByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

}
