package com.ai.st.microservice.quality.modules.attachments.application.add_attachment_to_product;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class AttachmentAssignerCommand implements Command {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long operatorCode;
    private final Attachment attachment;

    public AttachmentAssignerCommand(Long deliveryId, Long deliveryProductId, Long operatorCode,
            Attachment attachment) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.operatorCode = operatorCode;
        this.attachment = attachment;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long operatorCode() {
        return operatorCode;
    }

    public Attachment attachment() {
        return attachment;
    }

    public static class Attachment {

        private final String observations;

        public Attachment(String observations) {
            this.observations = observations;
        }

        public String observations() {
            return observations;
        }

        public boolean isXTF() {
            return this instanceof XTFAttachment;
        }
    }

    public static class XTFAttachment extends Attachment {

        private final byte[] bytes;
        private final String extension;
        private final String version;

        public XTFAttachment(String observations, byte[] bytes, String extension, String version) {
            super(observations);
            this.bytes = bytes;
            this.extension = extension;
            this.version = version;
        }

        public byte[] bytes() {
            return bytes;
        }

        public String extension() {
            return extension;
        }

        public String version() {
            return version;
        }
    }

    public static class FTPAttachment extends Attachment {

        private final String domain;
        private final String port;
        private final String username;
        private final String password;

        public FTPAttachment(String observations, String domain, String port, String username, String password) {
            super(observations);
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

    public static class DocumentAttachment extends Attachment {

        private final byte[] bytes;
        private final String extension;

        public DocumentAttachment(String observations, byte[] bytes, String extension) {
            super(observations);
            this.bytes = bytes;
            this.extension = extension;
        }

        public byte[] bytes() {
            return bytes;
        }

        public String extension() {
            return extension;
        }
    }

}
