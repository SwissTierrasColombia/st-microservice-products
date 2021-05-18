package com.ai.st.microservice.quality.modules.deliveries.application.FindAttachmentsFromProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.AttachmentProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class AttachmentsProductFinder {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public AttachmentsProductFinder(DeliveryProductAttachmentRepository attachmentRepository,
                                    DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository) {
        this.attachmentRepository = attachmentRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    public List<AttachmentProductResponse> find(AttachmentsProductFinderQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());

        verifyPermissions(deliveryId, deliveryProductId, query.role(), query.entityCode());

        return attachmentRepository.findByDeliveryProductId(deliveryProductId)
                .stream().map(this::mappingResponse).collect(Collectors.toList());
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
