package com.ai.st.microservice.quality.modules.attachments.domain.xtf;

import com.ai.st.microservice.quality.modules.attachments.domain.*;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;

import java.util.Date;

public final class DeliveryProductXTFAttachment extends DeliveryProductAttachment {

    private final XTFValid isValid;
    private final XTFUrl url;
    private final XTFVersion version;
    private final XTFStatus status;
    private final XTFReportRevisionUrl reportRevisionUrl;
    private final XTFReportObservations reportObservations;

    public DeliveryProductXTFAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentUUID uuid,
            DeliveryProductAttachmentObservations observations, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentDate deliveryProductAttachmentDate, XTFValid valid, XTFUrl url, XTFVersion version,
            XTFStatus status, XTFReportRevisionUrl reportRevisionUrl, XTFReportObservations reportObservations) {
        super(id, uuid, observations, deliveryProductId, deliveryProductAttachmentDate);
        this.isValid = valid;
        this.url = url;
        this.version = version;
        this.status = status;
        this.reportRevisionUrl = reportRevisionUrl;
        this.reportObservations = reportObservations;
    }

    public static DeliveryProductXTFAttachment create(DeliveryProductAttachmentUUID uuid,
            DeliveryProductAttachmentObservations observations, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentDate deliveryProductAttachmentDate, XTFValid valid, XTFUrl url, XTFVersion version,
            XTFStatus status) {
        return new DeliveryProductXTFAttachment(null, uuid, observations, deliveryProductId,
                deliveryProductAttachmentDate, valid, url, version, status, null, null);
    }

    public static DeliveryProductXTFAttachment fromPrimitives(Long id, String uuid, String observations,
            Long deliveryProductId, Date date, Boolean isValid, String url, String reportUrl, String reportObservations,
            String version, String status) {

        return new DeliveryProductXTFAttachment(new DeliveryProductAttachmentId(id),
                new DeliveryProductAttachmentUUID(uuid), new DeliveryProductAttachmentObservations(observations),
                new DeliveryProductId(deliveryProductId), new DeliveryProductAttachmentDate(date),
                new XTFValid(isValid), new XTFUrl(url), new XTFVersion(version),
                new XTFStatus(XTFStatus.valueOf(status)), new XTFReportRevisionUrl(reportUrl, true),
                new XTFReportObservations(reportObservations));
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

    public XTFReportRevisionUrl reportRevisionUrl() {
        return reportRevisionUrl;
    }

    public XTFReportObservations reportObservations() {
        return reportObservations;
    }

    public boolean accepted() {
        return (status.value().equals(XTFStatus.Status.ACCEPTED));
    }

    public boolean qualityInValidation() {
        return (status.value().equals(XTFStatus.Status.QUALITY_PROCESS_IN_VALIDATION));
    }

    public boolean hasReportRevisionURL() {
        return reportRevisionUrl.value() != null;
    }

}
