package com.ai.st.microservice.quality.modules.attachments.application.update_xtf_status;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class XTFStatusUpdaterCommand implements Command {

    public enum Status {ACCEPTED, REJECTED, QUALITY_PROCESS_FINISHED}

    private final Status status;
    private final String attachmentUUID;
    private final Long attachmentId;

    public XTFStatusUpdaterCommand(Status status, String attachmentUUID) {
        this.status = status;
        this.attachmentUUID = attachmentUUID;
        this.attachmentId = null;
    }

    public XTFStatusUpdaterCommand(Status status, Long attachmentId) {
        this.status = status;
        this.attachmentId = attachmentId;
        this.attachmentUUID = null;
    }

    public Status status() {
        return status;
    }

    public String attachmentUUID() {
        return attachmentUUID;
    }

    public Long attachmentId() {
        return attachmentId;
    }
}
