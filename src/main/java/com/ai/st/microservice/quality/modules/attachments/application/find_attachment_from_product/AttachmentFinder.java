package com.ai.st.microservice.quality.modules.attachments.application.find_attachment_from_product;

import com.ai.st.microservice.quality.modules.attachments.application.AttachmentProductResponse;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.attachments.domain.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentNotFound;
import com.ai.st.microservice.quality.modules.attachments.domain.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;

import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class AttachmentFinder implements QueryUseCase<AttachmentFinderQuery, AttachmentProductResponse> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public AttachmentFinder(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
            DeliveryProductAttachmentRepository attachmentRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public AttachmentProductResponse handle(AttachmentFinderQuery query) {

        DeliveryId deliveryId = DeliveryId.fromValue(query.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(query.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = DeliveryProductAttachmentId.fromValue(query.attachmentId());

        DeliveryProductAttachment attachment = verifyPermissions(deliveryId, deliveryProductId, attachmentId,
                query.isOnlyXTF());

        return mappingResponse(attachment);
    }

    private DeliveryProductAttachment verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentId attachmentId, boolean onlyXTF) {

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

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);
        if (deliveryProductAttachment == null) {
            throw new AttachmentNotFound();
        }

        DeliveryProductXTFAttachment attachmentXTF = (DeliveryProductXTFAttachment) deliveryProductAttachment;
        if (!attachmentXTF.isXTF() && onlyXTF) {
            throw new AttachmentNotFound();
        }

        // verify attachment belong to delivery product
        if (!deliveryProductAttachment.deliveryProductId().value().equals(deliveryProductId.value())) {
            throw new UnauthorizedToModifyDelivery("El adjunto no pertenece al producto.");
        }

        return attachmentXTF;
    }

    private AttachmentProductResponse mappingResponse(DeliveryProductAttachment attachment) {

        if (attachment instanceof DeliveryProductXTFAttachment) {
            return AttachmentProductResponse.fromAggregate((DeliveryProductXTFAttachment) attachment);
        }

        if (attachment instanceof DeliveryProductFTPAttachment) {
            return AttachmentProductResponse.fromAggregate((DeliveryProductFTPAttachment) attachment);
        }

        if (attachment instanceof DeliveryProductDocumentAttachment) {
            return AttachmentProductResponse.fromAggregate((DeliveryProductDocumentAttachment) attachment);
        }

        return null;
    }

}
