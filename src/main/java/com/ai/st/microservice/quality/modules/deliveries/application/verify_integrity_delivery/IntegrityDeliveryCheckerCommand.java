package com.ai.st.microservice.quality.modules.deliveries.application.verify_integrity_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class IntegrityDeliveryCheckerCommand implements Command {

    private final Long deliveryId;

    public IntegrityDeliveryCheckerCommand(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Long deliveryId() {
        return deliveryId;
    }
}
