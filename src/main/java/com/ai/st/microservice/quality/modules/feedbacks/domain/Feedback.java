package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.shared.domain.AggregateRoot;

public final class Feedback extends AggregateRoot {

    private final FeedbackId feedbackId;
    private final FeedbackComments comments;
    private final FeedbackURLAttachment urlAttachment;
    private final FeedbackDate date;

    public Feedback(FeedbackId feedbackId, FeedbackComments comments, FeedbackURLAttachment urlAttachment, FeedbackDate feedbackDate) {
        this.feedbackId = feedbackId;
        this.comments = comments;
        this.urlAttachment = urlAttachment;
        this.date = feedbackDate;
    }

    public static Feedback create(FeedbackComments comments, FeedbackURLAttachment urlAttachment, FeedbackDate feedbackDate) {
        return new Feedback(null, comments, urlAttachment, feedbackDate);
    }

    public FeedbackId feedbackId() {
        return feedbackId;
    }

    public FeedbackComments comments() {
        return comments;
    }

    public FeedbackURLAttachment urlAttachment() {
        return urlAttachment;
    }

    public FeedbackDate date() {
        return date;
    }

}
