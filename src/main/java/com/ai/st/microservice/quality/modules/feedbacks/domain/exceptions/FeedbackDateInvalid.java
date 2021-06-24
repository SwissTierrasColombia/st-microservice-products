package com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class FeedbackDateInvalid extends DomainError {

    public FeedbackDateInvalid() {
        super("feedback_date_invalid", "La fecha de la retroalimentación no es válida.");
    }
}
