package com.ai.st.microservice.quality.modules.deliveries.application.RemoveDelivery;

public final class DeliveryRemoverCommand {

    private final Long deliveryId;
    private final Long operatorCode;

    public DeliveryRemoverCommand(Long deliveryId, Long operatorCode) {
        this.deliveryId = deliveryId;
        this.operatorCode = operatorCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long operatorCode() {
        return operatorCode;
    }

}
