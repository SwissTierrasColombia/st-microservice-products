package com.ai.st.microservice.quality.modules.attachments.application;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.document.DeliveryProductDocumentAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.ftp.DeliveryProductFTPAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.shared.application.Response;

import java.util.Date;

public final class AttachmentProductResponse implements Response {

    private enum Types {
        XTF, FTP, DOCUMENT
    }

    private final Long attachmentId;
    private final Types type;
    private final AttachmentType data;
    private final Date attachmentDate;
    private final String observations;
    private final Long deliveryProductId;

    public AttachmentProductResponse(Long attachmentId, Types type, AttachmentType data, Date attachmentDate,
            String observations, Long deliveryProductId) {
        this.attachmentId = attachmentId;
        this.type = type;
        this.data = data;
        this.attachmentDate = attachmentDate;
        this.observations = observations;
        this.deliveryProductId = deliveryProductId;
    }

    public static AttachmentProductResponse fromAggregate(DeliveryProductXTFAttachment attachment) {
        XTF xtf = new XTF(attachment.valid().value(), attachment.status().value().name(), attachment.version().value(),
                attachment.hasReportRevisionURL(),
                attachment.hasReportRevisionURL() ? attachment.reportObservations().value() : null);
        return fromAggregate(attachment, Types.XTF, xtf);
    }

    public static AttachmentProductResponse fromAggregate(DeliveryProductFTPAttachment attachment) {
        FTP ftp = new FTP(attachment.domain().value(), attachment.port().value(), attachment.username().value(),
                attachment.password().value());
        return fromAggregate(attachment, Types.FTP, ftp);
    }

    public static AttachmentProductResponse fromAggregate(DeliveryProductDocumentAttachment attachment) {
        return fromAggregate(attachment, Types.DOCUMENT, null);
    }

    private static AttachmentProductResponse fromAggregate(DeliveryProductAttachment attachment, Types type,
            AttachmentType data) {
        return new AttachmentProductResponse(attachment.deliveryProductAttachmentId().value(), type, data,
                attachment.deliveryProductAttachmentDate().value(), attachment.observations().value(),
                attachment.deliveryProductId().value());
    }

    public Long attachmentId() {
        return attachmentId;
    }

    public Types type() {
        return type;
    }

    public Date attachmentDate() {
        return attachmentDate;
    }

    public String observations() {
        return observations;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public AttachmentType data() {
        return data;
    }

    private static abstract class AttachmentType {

    }

    private static class XTF extends AttachmentType {

        private final Boolean isValid;
        private final String status;
        private final String version;
        private final boolean hasReportRevision;
        private final String reportObservations;

        public XTF(Boolean isValid, String status, String version, boolean hasReportRevision,
                String reportObservations) {
            this.isValid = isValid;
            this.status = status;
            this.version = version;
            this.hasReportRevision = hasReportRevision;
            this.reportObservations = reportObservations;
        }

        public Boolean valid() {
            return isValid;
        }

        public String status() {
            return status;
        }

        public String version() {
            return version;
        }

        public boolean hasReportRevision() {
            return hasReportRevision;
        }

        public String reportObservations() {
            return reportObservations;
        }
    }

    private static class FTP extends AttachmentType {

        private final String domain;
        private final String port;
        private final String username;
        private final String password;

        public FTP(String domain, String port, String username, String password) {
            this.domain = domain;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        public String domain() {
            return domain;
        }

        public String port() {
            return port;
        }

        public String username() {
            return username;
        }

        public String password() {
            return password;
        }
    }

}
