package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.shared.domain.StringValueObject;

public final class FeedbackURLAttachment extends StringValueObject {

    public FeedbackURLAttachment(String value) {
        super(value);
    }

    public static FeedbackURLAttachment fromValue(String value) {
        return new FeedbackURLAttachment(value);
    }

}
