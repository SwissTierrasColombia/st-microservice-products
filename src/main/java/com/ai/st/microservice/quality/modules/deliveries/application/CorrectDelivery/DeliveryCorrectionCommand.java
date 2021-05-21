package com.ai.st.microservice.quality.modules.deliveries.application.CorrectDelivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryCorrectionCommand implements Command {

    private final Long deliveryId;
    private final Long operatorCode;

    public DeliveryCorrectionCommand(Long deliveryId, Long operatorCode) {
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
