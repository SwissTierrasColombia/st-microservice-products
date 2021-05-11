package com.ai.st.microservice.quality.modules.deliveries.domain.products;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;

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
                           ProductId productId, DeliveryProductStatusId deliveryProductStatusId, List<DeliveryProductAttachment> attachments) {
        this.deliveryProductId = deliveryProductId;
        this.deliveryProductDate = deliveryProductDate;
        this.deliveryProductObservations = deliveryProductObservations;
        this.productId = productId;
        this.deliveryProductStatusId = deliveryProductStatusId;
        this.attachments = attachments;
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
