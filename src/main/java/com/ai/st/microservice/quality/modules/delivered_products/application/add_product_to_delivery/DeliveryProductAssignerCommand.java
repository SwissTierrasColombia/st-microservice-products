package com.ai.st.microservice.quality.modules.delivered_products.application.add_product_to_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryProductAssignerCommand implements Command {

    private final Long deliveryId;
    private final Long productId;
    private final Long operatorCode;

    public DeliveryProductAssignerCommand(Long deliveryId, Long productId, Long operatorCode) {
        this.deliveryId = deliveryId;
        this.productId = productId;
        this.operatorCode = operatorCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long productId() {
        return productId;
    }

    public Long operatorCode() {
        return operatorCode;
    }

}
