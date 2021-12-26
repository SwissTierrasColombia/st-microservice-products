package com.ai.st.microservice.quality.modules.delivered_products.application.evaluate_product;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryProductEvaluatorCommand implements Command {

    public enum Statuses {ACCEPTED, REJECTED}

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long managerCode;
    private final Statuses state;

    public DeliveryProductEvaluatorCommand(Long deliveryId, Long deliveryProductId, Long managerCode, Statuses state) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.managerCode = managerCode;
        this.state = state;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long managerCode() {
        return managerCode;
    }

    public Statuses state() {
        return state;
    }

}
