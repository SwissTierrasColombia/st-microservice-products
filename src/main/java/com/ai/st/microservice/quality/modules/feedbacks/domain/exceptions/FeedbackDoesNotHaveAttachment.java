package com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FeedbackDoesNotHaveAttachment extends DomainError {

    public FeedbackDoesNotHaveAttachment() {
        super("feedback_does_not_have_attachment", "La retroalimentación no tiene adjunto.");
    }
}
