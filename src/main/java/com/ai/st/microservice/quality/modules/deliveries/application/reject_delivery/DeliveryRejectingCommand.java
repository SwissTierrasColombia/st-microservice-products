package com.ai.st.microservice.quality.modules.deliveries.application.reject_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryRejectingCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;
    private final String justification;

    public DeliveryRejectingCommand(Long deliveryId, Long managerCode, String justification) {
        this.deliveryId = deliveryId;
        this.managerCode = managerCode;
        this.justification = justification;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long managerCode() {
        return managerCode;
    }

    public String justification() {
        return justification;
    }
}
