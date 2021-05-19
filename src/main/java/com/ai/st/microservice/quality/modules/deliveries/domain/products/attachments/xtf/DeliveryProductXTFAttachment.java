package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.*;

import java.util.Date;

public final class DeliveryProductXTFAttachment extends DeliveryProductAttachment {

    private final XTFValid isValid;
    private final XTFUrl url;
    private final XTFVersion version;
    private final XTFStatus status;

    public DeliveryProductXTFAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentUUID uuid,
                                        DeliveryProductAttachmentObservations observations,
                                        DeliveryProductId deliveryProductId, DeliveryProductAttachmentDate deliveryProductAttachmentDate,
                                        XTFValid valid, XTFUrl url, XTFVersion version, XTFStatus status) {
        super(id, uuid, observations, deliveryProductId, deliveryProductAttachmentDate);
        this.isValid = valid;
        this.url = url;
        this.version = version;
        this.status = status;
    }

    public static DeliveryProductXTFAttachment create(DeliveryProductAttachmentUUID uuid, DeliveryProductAttachmentObservations observations,
                                                      DeliveryProductId deliveryProductId, DeliveryProductAttachmentDate deliveryProductAttachmentDate,
                                                      XTFValid valid, XTFUrl url, XTFVersion version, XTFStatus status) {
        return new DeliveryProductXTFAttachment(null, uuid, observations, deliveryProductId, deliveryProductAttachmentDate, valid, url, version, status);
    }

    public static DeliveryProductXTFAttachment fromPrimitives(Long id, String uuid, String observations, Long deliveryProductId,
                                                              Date date, Boolean isValid, String url, String version, String status) {

        return new DeliveryProductXTFAttachment(
                new DeliveryProductAttachmentId(id),
                new DeliveryProductAttachmentUUID(uuid),
                new DeliveryProductAttachmentObservations(observations),
                new DeliveryProductId(deliveryProductId),
                new DeliveryProductAttachmentDate(date),
                new XTFValid(isValid),
                new XTFUrl(url),
                new XTFVersion(version),
                new XTFStatus(XTFStatus.valueOf(status))
        );
    }

    public XTFValid valid() {
        return isValid;
    }

    public XTFUrl url() {
        return url;
    }

    public XTFVersion version() {
        return version;
    }

    public XTFStatus status() {
        return status;
    }

    public boolean accepted() {
        return (status.value().equals(XTFStatus.Status.ACCEPTED));
    }

}
