package com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FeedbackCommentsInvalid extends DomainError {

    public FeedbackCommentsInvalid() {
        super("feedback_comments_invalid", "La retroalimentación es requerida.");
    }

}
