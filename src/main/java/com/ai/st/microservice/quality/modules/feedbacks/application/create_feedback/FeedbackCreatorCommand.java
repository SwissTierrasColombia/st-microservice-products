package com.ai.st.microservice.quality.modules.feedbacks.application.create_feedback;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class FeedbackCreatorCommand implements Command {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long managerCode;
    private final String feedback;
    private final byte[] attachment;
    private final String attachmentExtension;

    public FeedbackCreatorCommand(Long deliveryId, Long deliveryProductId, Long managerCode,
                                  String feedback, byte[] attachment, String attachmentExtension) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.managerCode = managerCode;
        this.feedback = feedback;
        this.attachment = attachment;
        this.attachmentExtension = attachmentExtension;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long managerCode() {
        return managerCode;
    }

    public String feedback() {
        return feedback;
    }

    public byte[] attachment() {
        return attachment;
    }

    public String attachmentExtension() {
        return attachmentExtension;
    }

}
