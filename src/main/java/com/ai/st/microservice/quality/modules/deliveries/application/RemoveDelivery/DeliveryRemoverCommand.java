package com.ai.st.microservice.quality.modules.deliveries.application.RemoveDelivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryRemoverCommand implements Command {

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
