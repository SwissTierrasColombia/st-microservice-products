package com.ai.st.microservice.quality.modules.deliveries.application.SendDeliveryToManager;

public final class DeliveryToManagerSenderCommand {

    private final Long deliveryId;
    private final Long operatorCode;

    public DeliveryToManagerSenderCommand(Long deliveryId, Long operatorCode) {
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
