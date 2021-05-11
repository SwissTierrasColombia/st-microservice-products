package com.ai.st.microservice.quality.modules.deliveries.application.AddProductToDelivery;

public final class AddProductToDeliveryCommand {

    private final Long deliveryId;
    private final Long productId;
    private final Long operatorCode;

    public AddProductToDeliveryCommand(Long deliveryId, Long productId, Long operatorCode) {
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
