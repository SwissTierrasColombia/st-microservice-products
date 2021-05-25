package com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FeedbackNotFound extends DomainError {

    public FeedbackNotFound() {
        super("feedback_not_fount", "No se ha encontrado la retroalimentación.");
    }
}
