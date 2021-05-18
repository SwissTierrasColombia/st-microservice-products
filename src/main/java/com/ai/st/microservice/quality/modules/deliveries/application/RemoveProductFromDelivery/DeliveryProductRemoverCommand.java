package com.ai.st.microservice.quality.modules.deliveries.application.RemoveProductFromDelivery;

public final class DeliveryProductRemoverCommand {

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
