package com.ai.st.microservice.quality.modules.delivered_products.application;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.shared.application.Response;

import java.util.Date;

public final class DeliveryProductResponse implements Response {

    private final Long id;
    private final Date deliveryProductDate;
    private final String observations;
    private final Long productId;
    private final Long deliveryProductStatusId;

    public DeliveryProductResponse(Long id, Date deliveryProductDate, String observations, Long productId,
                                   Long deliveryProductStatusId) {
        this.id = id;
        this.deliveryProductDate = deliveryProductDate;
        this.observations = observations;
        this.productId = productId;
        this.deliveryProductStatusId = deliveryProductStatusId;
    }

    public static DeliveryProductResponse fromAggregate(DeliveryProduct deliveryProduct) {
        return new DeliveryProductResponse(
                deliveryProduct.deliveryProductId().value(),
                deliveryProduct.deliveryProductDate().value(),
                deliveryProduct.deliveryProductObservations().value(),
                deliveryProduct.productId().value(),
                deliveryProduct.deliveryProductStatusId().value()
        );
    }

    public Long id() {
        return id;
    }

    public Date deliveryProductDate() {
        return deliveryProductDate;
    }

    public String observations() {
        return observations;
    }

    public Long productId() {
        return productId;
    }

    public Long deliveryProductStatusId() {
        return deliveryProductStatusId;
    }

}
