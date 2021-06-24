package com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentFTPEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentFTPJPARepository extends CrudRepository<DeliveredProductAttachmentFTPEntity, Long> {

    DeliveredProductAttachmentFTPEntity findByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

    long deleteByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

}
