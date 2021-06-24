package com.ai.st.microservice.quality.modules.feedbacks.domain;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.shared.domain.AggregateRoot;

import java.util.Date;

public final class Feedback extends AggregateRoot {

    private final FeedbackId feedbackId;
    private final FeedbackComments comments;
    private final FeedbackURLAttachment urlAttachment;
    private final FeedbackDate date;
    private final DeliveryProductId deliveryProductId;

    public Feedback(FeedbackId feedbackId, FeedbackComments comments, FeedbackURLAttachment urlAttachment, FeedbackDate feedbackDate,
                    DeliveryProductId deliveryProductId) {
        this.feedbackId = feedbackId;
        this.comments = comments;
        this.urlAttachment = urlAttachment;
        this.date = feedbackDate;
        this.deliveryProductId = deliveryProductId;
    }

    public static Feedback create(FeedbackComments comments, FeedbackURLAttachment urlAttachment, FeedbackDate feedbackDate, DeliveryProductId deliveryProductId) {
        return new Feedback(null, comments, urlAttachment, feedbackDate, deliveryProductId);
    }

    public static Feedback fromPrimitives(Long id, String comments, String urlAttachment, Date date, Long deliveryProductId) {
        return new Feedback(
                FeedbackId.fromValue(id),
                FeedbackComments.fromValue(comments),
                FeedbackURLAttachment.fromValue(urlAttachment),
                FeedbackDate.fromValue(date),
                DeliveryProductId.fromValue(deliveryProductId));
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

    public DeliveryProductId deliveryProductId() {
        return deliveryProductId;
    }

    public boolean hasAttachment() {
        return urlAttachment != null && (urlAttachment.value() != null && !urlAttachment.value().isEmpty());
    }

}
