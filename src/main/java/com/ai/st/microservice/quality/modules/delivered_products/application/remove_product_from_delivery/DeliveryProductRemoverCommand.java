package com.ai.st.microservice.quality.modules.delivered_products.application.remove_product_from_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryProductRemoverCommand implements Command {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long operatorCode;

    public DeliveryProductRemoverCommand(Long deliveryId, Long deliveryProductId, Long operatorCode) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.operatorCode = operatorCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long operatorCode() {
        return operatorCode;
    }

}
