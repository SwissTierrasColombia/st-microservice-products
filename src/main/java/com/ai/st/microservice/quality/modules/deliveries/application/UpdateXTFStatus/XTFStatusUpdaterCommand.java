package com.ai.st.microservice.quality.modules.deliveries.application.UpdateXTFStatus;

public final class XTFStatusUpdaterCommand {

    public enum Status {ACCEPTED, REJECTED}

    private final Status status;
    private final String attachmentUUID;

    public XTFStatusUpdaterCommand(Status status, String attachmentUUID) {
        this.status = status;
        this.attachmentUUID = attachmentUUID;
    }

    public Status status() {
        return status;
    }

    public String attachmentUUID() {
        return attachmentUUID;
    }

}
