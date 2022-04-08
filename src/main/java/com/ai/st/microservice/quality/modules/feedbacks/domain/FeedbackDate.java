package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions.FeedbackDateInvalid;
import com.ai.st.microservice.quality.modules.shared.domain.DateObjectValue;

import java.util.Date;

public final class FeedbackDate extends DateObjectValue {

    private FeedbackDate(Date value) {
        super(value);
    }

    public static FeedbackDate fromValue(Date value) {
        ensureDate(value);
        return new FeedbackDate(value);
    }

    private static void ensureDate(Date value) {
        if (value == null)
            throw new FeedbackDateInvalid();
    }

}
