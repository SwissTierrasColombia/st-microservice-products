package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities;

public enum StatusXTFEnum {

    IN_VALIDATION("EN_VALIDACIÓN"),
    ACCEPTED("ACEPTADO"),
    REJECTED("RECHAZADO"),
    QUALITY_PROCESS_IN_VALIDATION("QUALITY_PROCESS_IN_VALIDATION"),
    QUALITY_PROCESS_FINISHED("QUALITY_PROCESS_FINISHED");

    private String status;

    StatusXTFEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
