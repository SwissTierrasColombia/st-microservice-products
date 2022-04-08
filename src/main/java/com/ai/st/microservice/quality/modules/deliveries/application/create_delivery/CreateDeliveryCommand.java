package com.ai.st.microservice.quality.modules.deliveries.application.create_delivery;

import com.ai.st.microservice.quality.modules.shared.application.Command;

import java.util.List;

public final class CreateDeliveryCommand implements Command {

    private final String municipalityCode;
    private final Long managerCode;
    private final Long operatorCode;
    private final Long userCode;
    private final String observations;
    private final List<Long> deliveryProducts;

    public CreateDeliveryCommand(String municipalityCode, Long managerCode, Long operatorCode, Long userCode,
            String observations, List<Long> deliveryProducts) {
        this.municipalityCode = municipalityCode;
        this.managerCode = managerCode;
        this.operatorCode = operatorCode;
        this.userCode = userCode;
        this.observations = observations;
        this.deliveryProducts = deliveryProducts;
    }

    public String municipalityCode() {
        return municipalityCode;
    }

    public Long managerCode() {
        return managerCode;
    }

    public Long operatorCode() {
        return operatorCode;
    }

    public Long userCode() {
        return userCode;
    }

    public String observations() {
        return observations;
    }

    public List<Long> deliveryProducts() {
        return deliveryProducts;
    }
}
