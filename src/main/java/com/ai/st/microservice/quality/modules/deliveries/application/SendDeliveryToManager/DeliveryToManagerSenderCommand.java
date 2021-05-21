package com.ai.st.microservice.quality.modules.deliveries.application.SendDeliveryToManager;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryToManagerSenderCommand implements Command {

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
