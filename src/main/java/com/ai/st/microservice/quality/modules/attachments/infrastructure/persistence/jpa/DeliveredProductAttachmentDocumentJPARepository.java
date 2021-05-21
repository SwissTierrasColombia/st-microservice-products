package com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentDocumentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentDocumentJPARepository extends CrudRepository<DeliveredProductAttachmentDocumentEntity, Long> {

    DeliveredProductAttachmentDocumentEntity findByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

    long deleteByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

}
