package com.ai.st.microservice.quality.modules.delivered_products.application.update_product_from_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryProductUpdaterCommand implements Command {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final String observations;
    private final Long operatorCode;

    public DeliveryProductUpdaterCommand(Long deliveryId, Long deliveryProductId, String observations, Long operatorCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.observations = observations;
        this.operatorCode = operatorCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public String observations() {
        return observations;
    }

    public Long operatorCode() {
        return operatorCode;
    }
}
