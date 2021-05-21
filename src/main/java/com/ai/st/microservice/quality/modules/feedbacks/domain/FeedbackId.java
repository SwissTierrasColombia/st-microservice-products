package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.shared.domain.LongObjectValue;

public final class FeedbackId extends LongObjectValue {

    public FeedbackId(Long value) {
        super(value);
    }

    public static FeedbackId fromValue(Long value) {
        return new FeedbackId(value);
    }

}
