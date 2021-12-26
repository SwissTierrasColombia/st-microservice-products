package com.ai.st.microservice.quality.modules.delivered_products.domain;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.shared.domain.AggregateRoot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class DeliveryProduct extends AggregateRoot {

    private final DeliveryProductId deliveryProductId;
    private final DeliveryProductDate deliveryProductDate;
    private final DeliveryProductObservations deliveryProductObservations;
    private final ProductId productId;
    private final DeliveryProductStatusId deliveryProductStatusId;
    private final DeliveryProductStatusDate deliveryProductStatusDate;
    private final List<DeliveryProductAttachment> attachments;

    public DeliveryProduct(DeliveryProductId deliveryProductId, DeliveryProductDate deliveryProductDate,
                           DeliveryProductObservations deliveryProductObservations,
                           ProductId productId, DeliveryProductStatusId deliveryProductStatusId,
                           DeliveryProductStatusDate deliveryProductStatusDate, List<DeliveryProductAttachment> attachments) {
        this.deliveryProductId = deliveryProductId;
        this.deliveryProductDate = deliveryProductDate;
        this.deliveryProductObservations = deliveryProductObservations;
        this.productId = productId;
        this.deliveryProductStatusId = deliveryProductStatusId;
        this.attachments = attachments;
        this.deliveryProductStatusDate = deliveryProductStatusDate;
    }

    public static DeliveryProduct fromPrimitives(Long deliveryProductId, Date deliveryProductDate, String observations, Long productId,
                                                 Long deliveryProductStatusId, Date deliveryProductStatusDate) {
        return new DeliveryProduct(
                new DeliveryProductId(deliveryProductId),
                new DeliveryProductDate(deliveryProductDate),
                new DeliveryProductObservations(observations),
                new ProductId(productId),
                new DeliveryProductStatusId(deliveryProductStatusId),
                new DeliveryProductStatusDate(deliveryProductStatusDate),
                new ArrayList<>()
        );
    }

    public static DeliveryProduct create(DeliveryProductDate deliveryProductDate,
                                         DeliveryProductObservations deliveryProductObservations,
                                         ProductId productId, DeliveryProductStatusId deliveryProductStatusId) {
        return new DeliveryProduct(null, deliveryProductDate, deliveryProductObservations,
                productId, deliveryProductStatusId,
                new DeliveryProductStatusDate(new Date()),
                new ArrayList<>());
    }

    public boolean isPending() {
        return deliveryProductStatusId.value().equals(DeliveryProductStatusId.PENDING);
    }

    public boolean isAccepted() {
        return deliveryProductStatusId.value().equals(DeliveryProductStatusId.ACCEPTED);
    }

    public boolean isRejected() {
        return deliveryProductStatusId.value().equals(DeliveryProductStatusId.REJECTED);
    }

    public DeliveryProductId deliveryProductId() {
        return deliveryProductId;
    }

    public DeliveryProductDate deliveryProductDate() {
        return deliveryProductDate;
    }

    public DeliveryProductObservations deliveryProductObservations() {
        return deliveryProductObservations;
    }

    public ProductId productId() {
        return productId;
    }

    public DeliveryProductStatusId deliveryProductStatusId() {
        return deliveryProductStatusId;
    }

    public List<DeliveryProductAttachment> attachments() {
        return attachments;
    }

    public DeliveryProductStatusDate deliveryProductStatusDate() {
        return deliveryProductStatusDate;
    }
}
