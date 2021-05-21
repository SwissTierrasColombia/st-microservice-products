package com.ai.st.microservice.quality.modules.attachments.application.get_attachment_url;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentNotFound;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentUnsupportedToDownload;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.*;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.application.StringResponse;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class AttachmentURLGetter implements QueryUseCase<AttachmentURLGetterQuery, StringResponse> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public AttachmentURLGetter(DeliveryProductAttachmentRepository attachmentRepository, DeliveryProductRepository deliveryProductRepository,
                               DeliveryRepository deliveryRepository) {
        this.attachmentRepository = attachmentRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    @Override
    public StringResponse handle(AttachmentURLGetterQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = new DeliveryProductAttachmentId(query.attachmentId());

        verifyPermissions(deliveryId, deliveryProductId, query.role(), query.entityCode());

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);
        checkAttachmentExits(deliveryProductAttachment);

        return new StringResponse(getPathFile(deliveryProductAttachment));
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
