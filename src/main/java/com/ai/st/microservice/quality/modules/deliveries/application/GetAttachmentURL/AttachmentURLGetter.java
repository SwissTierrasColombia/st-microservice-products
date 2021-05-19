package com.ai.st.microservice.quality.modules.deliveries.application.GetAttachmentURL;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.*;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class AttachmentURLGetter {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public AttachmentURLGetter(DeliveryProductAttachmentRepository attachmentRepository, DeliveryProductRepository deliveryProductRepository,
                               DeliveryRepository deliveryRepository) {
        this.attachmentRepository = attachmentRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    public String get(AttachmentURLGetterQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = new DeliveryProductAttachmentId(query.attachmentId());

        verifyPermissions(deliveryId, deliveryProductId, query.role(), query.entityCode());

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);
        checkAttachmentExits(deliveryProductAttachment);

        return getPathFile(deliveryProductAttachment);
    }

    private void verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId, Roles role, Long entityCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify delivery product exists
        DeliveryProduct deliveryProduct = deliveryProductRepository.search(deliveryProductId);
        if (deliveryProduct == null) {
            throw new DeliveryProductNotFound();
        }

        // verify owner of the delivery
        if (role.equals(Roles.OPERATOR)) {
            if (!delivery.deliveryBelongToOperator(OperatorCode.fromValue(entityCode))) {
                throw new UnauthorizedToSearchDelivery();
            }
        }
        if (role.equals(Roles.MANAGER)) {
            // verify status of the delivery
            if (!delivery.deliveryBelongToManager(ManagerCode.fromValue(entityCode)) || !delivery.isAvailableToManager()) {
                throw new UnauthorizedToSearchDelivery();
            }
        }

    }

    private void checkAttachmentExits(DeliveryProductAttachment attachment) {
        if (attachment == null) {
            throw new AttachmentNotFound();
        }
    }

    private String getPathFile(DeliveryProductAttachment deliveryProductAttachment) {

        if (deliveryProductAttachment instanceof DeliveryProductXTFAttachment) {
            DeliveryProductXTFAttachment xtf = (DeliveryProductXTFAttachment) deliveryProductAttachment;
            return xtf.url().value();
        }

        if (deliveryProductAttachment instanceof DeliveryProductDocumentAttachment) {
            DeliveryProductDocumentAttachment document = (DeliveryProductDocumentAttachment) deliveryProductAttachment;
            return document.documentUrl().value();
        }

        throw new AttachmentUnsupportedToDownload();
    }

}
