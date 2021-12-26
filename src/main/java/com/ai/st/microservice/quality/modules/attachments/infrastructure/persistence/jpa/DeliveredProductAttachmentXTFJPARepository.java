package com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductAttachmentXTFEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentXTFJPARepository extends CrudRepository<DeliveredProductAttachmentXTFEntity, Long> {

    DeliveredProductAttachmentXTFEntity findByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

    long deleteByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

}
