package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.shared.domain.AggregateRoot;

import java.util.Date;

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

    public static Feedback fromPrimitives(Long id, String comments, String urlAttachment, Date date) {
        return new Feedback(
                FeedbackId.fromValue(id),
                FeedbackComments.fromValue(comments),
                FeedbackURLAttachment.fromValue(urlAttachment),
                FeedbackDate.fromValue(date)
        );
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

    public boolean hasAttachment() {
        return urlAttachment != null && (urlAttachment.value() != null && !urlAttachment.value().isEmpty());
    }

}
