package com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence;

import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentUUID;
import com.ai.st.microservice.quality.modules.attachments.domain.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.XTFStatus;
import com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa.DeliveredProductAttachmentDocumentJPARepository;
import com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa.DeliveredProductAttachmentFTPJPARepository;
import com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa.DeliveredProductAttachmentJPARepository;
import com.ai.st.microservice.quality.modules.attachments.infrastructure.persistence.jpa.DeliveredProductAttachmentXTFJPARepository;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.*;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostgresDeliveryProductAttachmentRepository implements DeliveryProductAttachmentRepository {

    private final DeliveredProductAttachmentJPARepository deliveredProductAttachmentJPARepository;
    private final DeliveredProductAttachmentXTFJPARepository deliveredProductAttachmentXTFJPARepository;
    private final DeliveredProductAttachmentFTPJPARepository deliveredProductAttachmentFTPJPARepository;
    private final DeliveredProductAttachmentDocumentJPARepository deliveredProductAttachmentDocumentJPARepository;

    public PostgresDeliveryProductAttachmentRepository(DeliveredProductAttachmentJPARepository deliveredProductAttachmentJPARepository,
                                                       DeliveredProductAttachmentXTFJPARepository deliveredProductAttachmentXTFJPARepository,
                                                       DeliveredProductAttachmentFTPJPARepository deliveredProductAttachmentFTPJPARepository,
                                                       DeliveredProductAttachmentDocumentJPARepository deliveredProductAttachmentDocumentJPARepository) {
        this.deliveredProductAttachmentJPARepository = deliveredProductAttachmentJPARepository;
        this.deliveredProductAttachmentXTFJPARepository = deliveredProductAttachmentXTFJPARepository;
        this.deliveredProductAttachmentFTPJPARepository = deliveredProductAttachmentFTPJPARepository;
        this.deliveredProductAttachmentDocumentJPARepository = deliveredProductAttachmentDocumentJPARepository;
    }

    @Override
    public void save(DeliveryProductAttachment deliveryProductAttachment) {

        DeliveredProductEntity deliveryProduct = new DeliveredProductEntity();
        deliveryProduct.setId(deliveryProductAttachment.deliveryProductId().value());


        DeliveredProductAttachmentEntity attachmentEntity = new DeliveredProductAttachmentEntity();
        attachmentEntity.setUuid(deliveryProductAttachment.uuid().value());
        attachmentEntity.setCreatedAt(deliveryProductAttachment.deliveryProductAttachmentDate().value());
        attachmentEntity.setObservations(deliveryProductAttachment.observations().value());
        attachmentEntity.setDeliveredProduct(deliveryProduct);

        attachmentEntity = deliveredProductAttachmentJPARepository.save(attachmentEntity);

        if (deliveryProductAttachment instanceof DeliveryProductXTFAttachment) {
            saveXTFAttachment((DeliveryProductXTFAttachment) deliveryProductAttachment, attachmentEntity);
        }

        if (deliveryProductAttachment instanceof DeliveryProductFTPAttachment) {
            saveFTPAttachment((DeliveryProductFTPAttachment) deliveryProductAttachment, attachmentEntity);
        }

        if (deliveryProductAttachment instanceof DeliveryProductDocumentAttachment) {
            saveDocumentAttachment((DeliveryProductDocumentAttachment) deliveryProductAttachment, attachmentEntity);
        }

    }

    private void saveXTFAttachment(DeliveryProductXTFAttachment xtfAttachment,
                                   DeliveredProductAttachmentEntity attachmentEntity) {

        DeliveredProductAttachmentXTFEntity xtfEntity = new DeliveredProductAttachmentXTFEntity();
        xtfEntity.setDeliveredProductAttachment(attachmentEntity);
        xtfEntity.setUrl(xtfAttachment.url().value());
        xtfEntity.setValid(xtfAttachment.valid().value());
        xtfEntity.setVersion(xtfAttachment.version().value());
        xtfEntity.setStatus(mappingEnum(xtfAttachment.status()));

        deliveredProductAttachmentXTFJPARepository.save(xtfEntity);
    }

    private void saveFTPAttachment(DeliveryProductFTPAttachment ftpAttachment,
                                   DeliveredProductAttachmentEntity attachmentEntity) {

        DeliveredProductAttachmentFTPEntity ftpEntity = new DeliveredProductAttachmentFTPEntity();
        ftpEntity.setDeliveredProductAttachment(attachmentEntity);
        ftpEntity.setDomain(ftpAttachment.domain().value());
        ftpEntity.setPort(ftpAttachment.port().value());
        ftpEntity.setUsername(ftpAttachment.username().value());
        ftpEntity.setPassword(ftpAttachment.password().value());

        deliveredProductAttachmentFTPJPARepository.save(ftpEntity);
    }

    private void saveDocumentAttachment(DeliveryProductDocumentAttachment documentAttachment,
                                        DeliveredProductAttachmentEntity attachmentEntity) {

        DeliveredProductAttachmentDocumentEntity documentEntity = new DeliveredProductAttachmentDocumentEntity();
        documentEntity.setDeliveredProductAttachment(attachmentEntity);
        documentEntity.setUrl(documentAttachment.documentUrl().value());

        deliveredProductAttachmentDocumentJPARepository.save(documentEntity);
    }

    @Override
    public DeliveryProductAttachment search(DeliveryProductAttachmentId id) {

        DeliveredProductAttachmentEntity deliveredProductAttachmentEntity =
                deliveredProductAttachmentJPARepository.findById(id.value()).orElse(null);

        if (deliveredProductAttachmentEntity != null) {
            return handleFind(deliveredProductAttachmentEntity);
        }

        return null;
    }

    @Override
    public DeliveryProductAttachment search(DeliveryProductAttachmentUUID uuid) {

        DeliveredProductAttachmentEntity deliveredProductAttachmentEntity =
                deliveredProductAttachmentJPARepository.findByUuid(uuid.value());

        if (deliveredProductAttachmentEntity != null) {
            return handleFind(deliveredProductAttachmentEntity);
        }

        return null;
    }

    private DeliveryProductAttachment handleFind(DeliveredProductAttachmentEntity deliveredProductAttachmentEntity) {

        DeliveredProductAttachmentXTFEntity xtfEntity =
                deliveredProductAttachmentXTFJPARepository.findByDeliveredProductAttachment(deliveredProductAttachmentEntity);
        if (xtfEntity != null) {
            return DeliveryProductXTFAttachment.fromPrimitives(
                    deliveredProductAttachmentEntity.getId(), deliveredProductAttachmentEntity.getUuid(), deliveredProductAttachmentEntity.getObservations(),
                    deliveredProductAttachmentEntity.getDeliveredProduct().getId(), deliveredProductAttachmentEntity.getCreatedAt(),
                    xtfEntity.getValid(), xtfEntity.getUrl(), xtfEntity.getVersion(), xtfEntity.getStatus().name()
            );
        }

        DeliveredProductAttachmentFTPEntity ftpEntity =
                deliveredProductAttachmentFTPJPARepository.findByDeliveredProductAttachment(deliveredProductAttachmentEntity);
        if (ftpEntity != null) {
            return DeliveryProductFTPAttachment.fromPrimitives(
                    deliveredProductAttachmentEntity.getId(), deliveredProductAttachmentEntity.getUuid(), deliveredProductAttachmentEntity.getObservations(),
                    deliveredProductAttachmentEntity.getDeliveredProduct().getId(), deliveredProductAttachmentEntity.getCreatedAt(),
                    ftpEntity.getDomain(), ftpEntity.getPort(), ftpEntity.getUsername(), ftpEntity.getPassword()
            );
        }

        DeliveredProductAttachmentDocumentEntity documentEntity =
                deliveredProductAttachmentDocumentJPARepository.findByDeliveredProductAttachment(deliveredProductAttachmentEntity);
        if (documentEntity != null) {
            return DeliveryProductDocumentAttachment.fromPrimitives(
                    deliveredProductAttachmentEntity.getId(), deliveredProductAttachmentEntity.getUuid(), deliveredProductAttachmentEntity.getObservations(),
                    deliveredProductAttachmentEntity.getDeliveredProduct().getId(), deliveredProductAttachmentEntity.getCreatedAt(),
                    documentEntity.getUrl()
            );
        }

        return null;
    }

    @Override
    public void updateXTFStatus(DeliveryProductAttachmentUUID uuid, XTFStatus status) {

        DeliveredProductAttachmentEntity deliveredProductAttachmentEntity =
                deliveredProductAttachmentJPARepository.findByUuid(uuid.value());

        if (deliveredProductAttachmentEntity != null) {

            DeliveredProductAttachmentXTFEntity xtfEntity =
                    deliveredProductAttachmentXTFJPARepository.findByDeliveredProductAttachment(deliveredProductAttachmentEntity);

            StatusXTFEnum statusEntity = mappingEnum(status);
            xtfEntity.setStatus(statusEntity);


            Boolean isValid = null;
            if (status.value().equals(XTFStatus.Status.ACCEPTED)) {
                isValid = true;
            } else if (status.value().equals(XTFStatus.Status.REJECTED)) {
                isValid = false;
            }
            xtfEntity.setValid(isValid);

            deliveredProductAttachmentXTFJPARepository.save(xtfEntity);

        }

    }

    private StatusXTFEnum mappingEnum(XTFStatus status) {
        switch (status.value()) {
            case ACCEPTED:
                return StatusXTFEnum.ACCEPTED;
            case REJECTED:
                return StatusXTFEnum.REJECTED;
            case IN_VALIDATION:
            default:
                return StatusXTFEnum.IN_VALIDATION;
        }
    }

    @Override
    public List<DeliveryProductAttachment> findByDeliveryProductId(DeliveryProductId deliveryProductId) {

        DeliveredProductEntity deliveredProduct = new DeliveredProductEntity();
        deliveredProduct.setId(deliveryProductId.value());

        return deliveredProductAttachmentJPARepository.findByDeliveredProduct(deliveredProduct)
                .stream().map(this::handleFind).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remove(DeliveryProductAttachmentId id) {

        DeliveredProductAttachmentEntity deliveredProductAttachmentEntity = new DeliveredProductAttachmentEntity();
        deliveredProductAttachmentEntity.setId(id.value());

        deliveredProductAttachmentXTFJPARepository.deleteByDeliveredProductAttachment(deliveredProductAttachmentEntity);
        deliveredProductAttachmentFTPJPARepository.deleteByDeliveredProductAttachment(deliveredProductAttachmentEntity);
        deliveredProductAttachmentDocumentJPARepository.deleteByDeliveredProductAttachment(deliveredProductAttachmentEntity);
        deliveredProductAttachmentJPARepository.deleteById(id.value());
    }

}
