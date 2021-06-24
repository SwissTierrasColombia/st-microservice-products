package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions.FeedbackCommentsInvalid;
import com.ai.st.microservice.quality.modules.shared.domain.StringValueObject;

public final class FeedbackComments extends StringValueObject {

    private FeedbackComments(String value) {
        super(value);
    }

    public static FeedbackComments fromValue(String value) {
        ensureComments(value);
        return new FeedbackComments(value);
    }

    private static void ensureComments(String value) {
        if (value == null || value.isEmpty())
            throw new FeedbackCommentsInvalid();
    }

}
