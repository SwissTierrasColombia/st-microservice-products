package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentDocumentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentDocumentJPARepository extends CrudRepository<DeliveredProductAttachmentDocumentEntity, Long> {

    DeliveredProductAttachmentDocumentEntity findByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

    long deleteByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

}
