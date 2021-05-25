package com.ai.st.microservice.quality.modules.deliveries.application.reject_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryRejectingCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;

    public DeliveryRejectingCommand(Long deliveryId, Long managerCode) {
        this.deliveryId = deliveryId;
        this.managerCode = managerCode;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long managerCode() {
        return managerCode;
    }

}
