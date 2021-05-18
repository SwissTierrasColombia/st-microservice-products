package com.ai.st.microservice.quality.modules.deliveries.application.GetAttachmentURL;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcherQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.AttachmentNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class AttachmentURLGetter {

    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final DeliveryProductSearcher deliveryProductSearcher;

    public AttachmentURLGetter(DeliveryProductAttachmentRepository attachmentRepository, DeliveryProductRepository deliveryProductRepository,
                               DeliveryRepository deliveryRepository) {
        this.attachmentRepository = attachmentRepository;
        this.deliveryProductSearcher = new DeliveryProductSearcher(deliveryProductRepository, deliveryRepository);
    }

    public String get(AttachmentURLGetterQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = new DeliveryProductAttachmentId(query.attachmentId());

        verifyPermissions(deliveryId.value(), deliveryProductId.value(), query.role(), query.entityCode());

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);
        checkAttachmentExits(deliveryProductAttachment);

        return getPathFile(deliveryProductAttachment);
    }

    private void verifyPermissions(Long deliveryId, Long deliveryProductId, Roles role, Long entityCode) {
        deliveryProductSearcher.search(new DeliveryProductSearcherQuery(
                deliveryId, deliveryProductId, role, entityCode
        ));
    }

    private void checkAttachmentExits(DeliveryProductAttachment attachment) {
        if (attachment == null) {
            throw new AttachmentNotFound();
        }
    }

    private String getPathFile(DeliveryProductAttachment deliveryProductAttachment) {

        String pathFile = null;

        if (deliveryProductAttachment instanceof DeliveryProductXTFAttachment) {
            DeliveryProductXTFAttachment xtf = (DeliveryProductXTFAttachment) deliveryProductAttachment;
            pathFile = xtf.url().value();
        }

        if (deliveryProductAttachment instanceof DeliveryProductDocumentAttachment) {
            DeliveryProductDocumentAttachment document = (DeliveryProductDocumentAttachment) deliveryProductAttachment;
            pathFile = document.documentUrl().value();
        }

        return pathFile;
    }

}
