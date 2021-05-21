package com.ai.st.microservice.quality.modules.deliveries.application.VerifyIntegrityDelivery;

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
