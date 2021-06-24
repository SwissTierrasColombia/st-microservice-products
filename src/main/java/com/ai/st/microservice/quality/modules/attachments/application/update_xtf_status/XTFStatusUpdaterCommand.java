package com.ai.st.microservice.quality.modules.attachments.application.update_xtf_status;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class XTFStatusUpdaterCommand implements Command {

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
