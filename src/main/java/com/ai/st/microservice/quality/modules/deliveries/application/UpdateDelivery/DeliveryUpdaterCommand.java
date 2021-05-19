package com.ai.st.microservice.quality.modules.deliveries.application.UpdateDelivery;

public final class DeliveryUpdaterCommand {

    private final Long deliveryId;
    private final String observations;
    private final Long operatorCode;

    public DeliveryUpdaterCommand(Long deliveryId, String observations, Long operatorCode) {
        this.deliveryId = deliveryId;
        this.observations = observations;
        this.operatorCode = operatorCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public String observations() {
        return observations;
    }

    public Long operatorCode() {
        return operatorCode;
    }
}
