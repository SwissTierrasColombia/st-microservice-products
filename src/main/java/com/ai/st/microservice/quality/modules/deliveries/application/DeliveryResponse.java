package com.ai.st.microservice.quality.modules.deliveries.application;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.shared.application.Response;

import java.util.Date;

public final class DeliveryResponse implements Response {

    private final Long id;
    private final String code;
    private final Date deliveryDate;
    private final Long managerCode;
    private final String municipalityCode;
    private final String observations;
    private final Long operatorCode;
    private final Long userCode;
    private final Long deliveryStatusId;
    private final String departmentName;
    private final String municipalityName;
    private final String managerName;
    private final String operatorName;

    public DeliveryResponse(Long id, String code, Date deliveryDate, Long managerCode, String municipalityCode,
                            String observations, Long operatorCode, Long userCode, Long deliveryStatusId, String departmentName,
                            String municipalityName, String managerName, String operatorName) {
        this.id = id;
        this.code = code;
        this.deliveryDate = deliveryDate;
        this.managerCode = managerCode;
        this.municipalityCode = municipalityCode;
        this.observations = observations;
        this.operatorCode = operatorCode;
        this.deliveryStatusId = deliveryStatusId;
        this.userCode = userCode;
        this.departmentName = departmentName;
        this.municipalityName = municipalityName;
        this.managerName = managerName;
        this.operatorName = operatorName;
    }

    public static DeliveryResponse fromAggregate(Delivery delivery) {
        return new DeliveryResponse(delivery.id().value(),
                delivery.code().value(),
                delivery.deliveryDate().value(),
                delivery.manager().value(),
                delivery.municipality().value(),
                delivery.observations().value(),
                delivery.operator().value(),
                delivery.user().value(),
                delivery.deliveryStatusId().value(),
                delivery.departmentName().value(),
                delivery.municipalityName().value(),
                delivery.managerName().value(),
                delivery.operatorName().value());
    }

    public Long id() {
        return id;
    }

    public String code() {
        return code;
    }

    public Date deliveryDate() {
        return deliveryDate;
    }

    public Long managerCode() {
        return managerCode;
    }

    public String municipalityCode() {
        return municipalityCode;
    }

    public String observations() {
        return observations;
    }

    public Long operatorCode() {
        return operatorCode;
    }

    public Long deliveryStatusId() {
        return deliveryStatusId;
    }

    public Long userCode() {
        return userCode;
    }

    public String departmentName() {
        return departmentName;
    }

    public String municipalityName() {
        return municipalityName;
    }

    public String managerName() {
        return managerName;
    }

    public String operatorName() {
        return operatorName;
    }
}
