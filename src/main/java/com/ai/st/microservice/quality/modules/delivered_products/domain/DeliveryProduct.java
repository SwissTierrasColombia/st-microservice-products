package com.ai.st.microservice.quality.modules.delivered_products.domain;

import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class DeliveryProduct {

    private final DeliveryProductId deliveryProductId;
    private final DeliveryProductDate deliveryProductDate;
    private final DeliveryProductObservations deliveryProductObservations;
    private final ProductId productId;
    private final DeliveryProductStatusId deliveryProductStatusId;
    private final List<DeliveryProductAttachment> attachments;

    public DeliveryProduct(DeliveryProductId deliveryProductId, DeliveryProductDate deliveryProductDate,
                           DeliveryProductObservations deliveryProductObservations,
                           ProductId productId, DeliveryProductStatusId deliveryProductStatusId,
                           List<DeliveryProductAttachment> attachments) {
        this.deliveryProductId = deliveryProductId;
        this.deliveryProductDate = deliveryProductDate;
        this.deliveryProductObservations = deliveryProductObservations;
        this.productId = productId;
        this.deliveryProductStatusId = deliveryProductStatusId;
        this.attachments = attachments;
    }

    public static DeliveryProduct fromPrimitives(Long deliveryProductId, Date deliveryProductDate, String observations, Long productId,
                                                 Long deliveryProductStatusId) {
        return new DeliveryProduct(
                new DeliveryProductId(deliveryProductId),
                new DeliveryProductDate(deliveryProductDate),
                new DeliveryProductObservations(observations),
                new ProductId(productId),
                new DeliveryProductStatusId(deliveryProductStatusId),
                new ArrayList<>()
        );
    }

    public static DeliveryProduct create(DeliveryProductDate deliveryProductDate,
                                         DeliveryProductObservations deliveryProductObservations,
                                         ProductId productId, DeliveryProductStatusId deliveryProductStatusId) {
        return new DeliveryProduct(null, deliveryProductDate, deliveryProductObservations,
                productId, deliveryProductStatusId, new ArrayList<>());
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

}
