package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductAttachmentXTFEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeliveredProductAttachmentXTFJPARepository extends CrudRepository<DeliveredProductAttachmentXTFEntity, Long> {

    DeliveredProductAttachmentXTFEntity findByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

    long deleteByDeliveredProductAttachment(DeliveredProductAttachmentEntity attachment);

}
