package com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_operator;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryToOperatorSenderCommand implements Command {

    private final Long deliveryId;
    private final Long managerCode;

    public DeliveryToOperatorSenderCommand(Long deliveryId, Long managerCode) {
        this.deliveryId = deliveryId;
        this.managerCode = managerCode;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public Long getManagerCode() {
        return managerCode;
    }

}
