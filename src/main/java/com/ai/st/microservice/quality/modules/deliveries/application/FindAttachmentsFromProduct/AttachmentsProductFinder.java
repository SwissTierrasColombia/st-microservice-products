package com.ai.st.microservice.quality.modules.deliveries.application.FindAttachmentsFromProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.AttachmentProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcherQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class AttachmentsProductFinder {

    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final DeliveryProductSearcher deliveryProductSearcher;

    public AttachmentsProductFinder(DeliveryProductAttachmentRepository attachmentRepository, DeliveryProductRepository deliveryProductRepository,
                                    DeliveryRepository deliveryRepository) {
        this.attachmentRepository = attachmentRepository;
        this.deliveryProductSearcher = new DeliveryProductSearcher(deliveryProductRepository, deliveryRepository);
    }

    public List<AttachmentProductResponse> find(AttachmentsProductFinderQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());

        verifyPermissions(deliveryId.value(), deliveryProductId.value(), query.role(), query.entityCode());

        return attachmentRepository.findByDeliveryProductId(deliveryProductId)
                .stream().map(this::mappingResponse).collect(Collectors.toList());
    }

    private void verifyPermissions(Long deliveryId, Long deliveryProductId, Roles role, Long entityCode) {
        deliveryProductSearcher.search(new DeliveryProductSearcherQuery(
                deliveryId, deliveryProductId, role, entityCode
        ));
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
