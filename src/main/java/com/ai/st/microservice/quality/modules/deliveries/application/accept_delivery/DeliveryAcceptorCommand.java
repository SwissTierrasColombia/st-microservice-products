package com.ai.st.microservice.quality.modules.deliveries.application.accept_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryAcceptorCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;
    private final String justification;

    public DeliveryAcceptorCommand(Long deliveryId, Long managerCode, String justification) {
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
