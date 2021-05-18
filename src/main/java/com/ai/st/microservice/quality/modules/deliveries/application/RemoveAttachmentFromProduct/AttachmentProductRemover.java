package com.ai.st.microservice.quality.modules.deliveries.application.RemoveAttachmentFromProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcherQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

@Service
public final class AttachmentProductRemover {

    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final DeliveryProductSearcher deliveryProductSearcher;
    private final StoreFile storeFile;

    public AttachmentProductRemover(DeliveryProductAttachmentRepository attachmentRepository, DeliveryProductRepository deliveryProductRepository,
                                    DeliveryRepository deliveryRepository, StoreFile storeFile) {
        this.storeFile = storeFile;
        this.attachmentRepository = attachmentRepository;
        this.deliveryProductSearcher = new DeliveryProductSearcher(deliveryProductRepository, deliveryRepository);
    }

    public void remove(AttachmentProductRemoverCommand command) {

        DeliveryId deliveryId = new DeliveryId(command.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(command.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = new DeliveryProductAttachmentId(command.attachmentId());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());

        verifyPermissions(deliveryId.value(), deliveryProductId.value(), operatorCode.value());

        DeliveryProductAttachment deliveryProductAttachment = attachmentRepository.search(attachmentId);

        if (deliveryProductAttachment != null) {
            deleteStorage(deliveryProductAttachment);
            attachmentRepository.remove(attachmentId);
        }

    }

    private void verifyPermissions(Long deliveryId, Long deliveryProductId, Long operatorCode) {
        deliveryProductSearcher.search(new DeliveryProductSearcherQuery(
                deliveryId, deliveryProductId, Roles.OPERATOR, operatorCode
        ));
    }

    private void deleteStorage(DeliveryProductAttachment deliveryProductAttachment) {

        String pathFile = null;

        if (deliveryProductAttachment instanceof DeliveryProductXTFAttachment) {
            DeliveryProductXTFAttachment xtf = (DeliveryProductXTFAttachment) deliveryProductAttachment;
            pathFile = xtf.url().value();
        }

        if (deliveryProductAttachment instanceof DeliveryProductDocumentAttachment) {
            DeliveryProductDocumentAttachment document = (DeliveryProductDocumentAttachment) deliveryProductAttachment;
            pathFile = document.documentUrl().value();
        }

        storeFile.deleteFile(pathFile);
    }

}
