package com.ai.st.microservice.quality.modules.deliveries.application.AddAttachmentToProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct.DeliveryProductSearcherQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.AttachmentUnsupported;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentDate;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentObservations;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.document.DocumentUrl;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.ftp.*;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.*;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;

import com.ai.st.microservice.quality.modules.shared.domain.contracts.ILIMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

import java.util.UUID;

@Service
public final class AttachmentAssigner {

    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final DeliveryProductSearcher deliveryProductSearcher;
    private final DateTime dateTime;
    private final StoreFile storeFile;
    private final ILIMicroservice iliMicroservice;

    public AttachmentAssigner(DeliveryProductAttachmentRepository attachmentRepository, DeliveryRepository deliveryRepository,
                              DeliveryProductRepository deliveryProductRepository, DateTime dateTime, StoreFile storeFile, ILIMicroservice iliMicroservice) {
        this.attachmentRepository = attachmentRepository;
        this.dateTime = dateTime;
        this.storeFile = storeFile;
        this.iliMicroservice = iliMicroservice;
        this.deliveryProductSearcher = new DeliveryProductSearcher(deliveryProductRepository, deliveryRepository);
    }

    public void assign(AttachmentAssignerCommand command) {

        DeliveryId deliveryId = new DeliveryId(command.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(command.deliveryProductId());
        OperatorCode operatorCode = new OperatorCode(command.operatorCode());

        verifyDeliveryProductExists(deliveryId.value(), deliveryProductId.value(), operatorCode.value());

        DeliveryProductAttachment attachment = handleAttachment(command.attachment(), deliveryId, deliveryProductId);

        attachmentRepository.save(attachment);
    }

    private void verifyDeliveryProductExists(Long deliveryId, Long deliveryProductId, Long operatorCode) {
        deliveryProductSearcher.search(new DeliveryProductSearcherQuery(
                deliveryId, deliveryProductId, Roles.OPERATOR, operatorCode
        ));
    }

    private DeliveryProductAttachment handleAttachment(AttachmentAssignerCommand.Attachment attachment,
                                                       DeliveryId deliveryId,
                                                       DeliveryProductId deliveryProductId) {

        DeliveryProductAttachmentDate attachmentDate = new DeliveryProductAttachmentDate(dateTime.now());

        if (attachment instanceof AttachmentAssignerCommand.XTFAttachment) {
            return handleXTFAttachment((AttachmentAssignerCommand.XTFAttachment) attachment, deliveryId, deliveryProductId, attachmentDate);
        }

        if (attachment instanceof AttachmentAssignerCommand.FTPAttachment) {
            return handleFTPAttachment((AttachmentAssignerCommand.FTPAttachment) attachment, deliveryProductId, attachmentDate);
        }

        if (attachment instanceof AttachmentAssignerCommand.DocumentAttachment) {
            return handleDocumentAttachment((AttachmentAssignerCommand.DocumentAttachment) attachment, deliveryId, deliveryProductId, attachmentDate);
        }

        throw new AttachmentUnsupported();
    }

    private DeliveryProductXTFAttachment handleXTFAttachment(AttachmentAssignerCommand.XTFAttachment attachment,
                                                             DeliveryId deliveryId,
                                                             DeliveryProductId deliveryProductId, DeliveryProductAttachmentDate attachmentDate) {

        String namespace = buildNamespace(deliveryId);
        String pathUrl = storeFile.storeFilePermanently(attachment.bytes(), attachment.extension(), namespace);

        DeliveryProductAttachmentUUID identifierUUID = new DeliveryProductAttachmentUUID(UUID.randomUUID().toString());

        iliMicroservice.sendToValidation(identifierUUID, pathUrl, false, false);

        return DeliveryProductXTFAttachment.create(
                identifierUUID,
                new DeliveryProductAttachmentObservations(attachment.observations()),
                deliveryProductId,
                attachmentDate,
                new XTFValid(null),
                new XTFUrl(pathUrl),
                new XTFVersion("1.0"),
                new XTFStatus(XTFStatus.Status.IN_VALIDATION)
        );
    }

    private DeliveryProductFTPAttachment handleFTPAttachment(AttachmentAssignerCommand.FTPAttachment attachment,
                                                             DeliveryProductId deliveryProductId, DeliveryProductAttachmentDate attachmentDate) {
        return DeliveryProductFTPAttachment.create(
                new DeliveryProductAttachmentUUID(UUID.randomUUID().toString()),
                new DeliveryProductAttachmentObservations(attachment.observations()),
                deliveryProductId,
                attachmentDate,
                new FTPDomain(attachment.domain()),
                new FTPPort(attachment.port()),
                new FTPUsername(attachment.username()),
                new FTPPassword(attachment.password())
        );
    }

    private DeliveryProductDocumentAttachment handleDocumentAttachment(AttachmentAssignerCommand.DocumentAttachment attachment,
                                                                       DeliveryId deliveryId,
                                                                       DeliveryProductId deliveryProductId, DeliveryProductAttachmentDate attachmentDate) {

        String namespace = buildNamespace(deliveryId);
        String pathUrl = storeFile.storeFilePermanently(attachment.bytes(), attachment.extension(), namespace);

        return DeliveryProductDocumentAttachment.create(
                new DeliveryProductAttachmentUUID(UUID.randomUUID().toString()),
                new DeliveryProductAttachmentObservations(attachment.observations()),
                deliveryProductId,
                attachmentDate,
                new DocumentUrl(pathUrl)
        );
    }

    private String buildNamespace(DeliveryId deliveryId) {
        return String.format("/entregas/%d", deliveryId.value());
    }

}
