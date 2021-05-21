package com.ai.st.microservice.quality.modules.attachments.application.find_attachments_from_product;

import com.ai.st.microservice.quality.modules.attachments.application.AttachmentProductResponse;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.application.ListResponse;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class AttachmentsProductFinder implements QueryUseCase<AttachmentsProductFinderQuery, ListResponse<AttachmentProductResponse>> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public AttachmentsProductFinder(DeliveryProductAttachmentRepository attachmentRepository,
                                    DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository) {
        this.attachmentRepository = attachmentRepository;
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    @Override
    public ListResponse<AttachmentProductResponse> handle(AttachmentsProductFinderQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());

        verifyPermissions(deliveryId, deliveryProductId, query.role(), query.entityCode());

        return new ListResponse<>(attachmentRepository.findByDeliveryProductId(deliveryProductId)
                .stream().map(this::mappingResponse).collect(Collectors.toList()));
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
