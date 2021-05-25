package com.ai.st.microservice.quality.modules.deliveries.application.accept_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryAcceptorCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;

    public DeliveryAcceptorCommand(Long deliveryId, Long managerCode) {
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
