package com.ai.st.microservice.quality.modules.feedbacks.application;

import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;
import com.ai.st.microservice.quality.modules.shared.application.Response;

import java.util.Date;

public final class FeedbackResponse implements Response {

    private final Long feedbackId;
    private final String feedback;
    private final Date feedbackDate;
    private final boolean hasAttachment;

    public FeedbackResponse(Long feedbackId, String feedback, Date feedbackDate, boolean hasAttachment) {
        this.feedbackId = feedbackId;
        this.feedback = feedback;
        this.feedbackDate = feedbackDate;
        this.hasAttachment = hasAttachment;
    }

    public static FeedbackResponse fromAggregate(Feedback feedback) {
        return new FeedbackResponse(
                feedback.feedbackId().value(),
                feedback.comments().value(),
                feedback.date().value(),
                feedback.hasAttachment()
        );
    }

    public Long feedbackId() {
        return feedbackId;
    }

    public String feedback() {
        return feedback;
    }

    public Date feedbackDate() {
        return feedbackDate;
    }

    public boolean hasAttachment() {
        return hasAttachment;
    }

}
