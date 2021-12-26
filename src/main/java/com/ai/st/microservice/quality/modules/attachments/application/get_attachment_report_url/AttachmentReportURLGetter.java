package com.ai.st.microservice.quality.modules.attachments.application.get_attachment_report_url;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.AttachmentNotFound;
import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.RemovingAttachmentToProductFailed;
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
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.application.StringResponse;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class AttachmentReportURLGetter implements QueryUseCase<AttachmentReportURLGetterQuery, StringResponse> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public AttachmentReportURLGetter(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository, DeliveryProductAttachmentRepository attachmentRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public StringResponse handle(AttachmentReportURLGetterQuery query) {

        DeliveryId deliveryId = DeliveryId.fromValue(query.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(query.deliveryProductId());
        DeliveryProductAttachmentId attachmentId = DeliveryProductAttachmentId.fromValue(query.attachmentId());
        ManagerCode managerCode = ManagerCode.fromValue(query.managerCode());

        DeliveryProductXTFAttachment attachment = verifyPermissions(deliveryId, deliveryProductId, attachmentId, managerCode);

        return new StringResponse(attachment.reportRevisionUrl().value());
    }

    private DeliveryProductXTFAttachment verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
                                                           DeliveryProductAttachmentId attachmentId, ManagerCode managerCode) {

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

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify type attachment
        if (!deliveryProductAttachment.isXTF()) {
            throw new UnauthorizedToModifyDelivery("No se puede descargar el reporte de revisión porque el archivo no es un XTF.");
        }

        // verify status of xtf attachment
        DeliveryProductXTFAttachment attachmentXTF = (DeliveryProductXTFAttachment) deliveryProductAttachment;
        if (!attachmentXTF.hasReportRevisionURL()) {
            throw new UnauthorizedToModifyDelivery("No existe un reporte de revisión para el archivo.");
        }

        // verify attachment belong to delivery product
        if (!deliveryProductAttachment.deliveryProductId().value().equals(deliveryProductId.value())) {
            throw new RemovingAttachmentToProductFailed("El adjunto no pertenece al producto.");
        }

        return attachmentXTF;
    }

}
