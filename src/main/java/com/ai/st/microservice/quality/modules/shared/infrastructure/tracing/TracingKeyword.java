package com.ai.st.microservice.quality.modules.shared.infrastructure.tracing;

public enum TracingKeyword {
    USER_ID("userId"), USER_NAME("username"), USER_EMAIL("userEmail"), MANAGER_ID("managerId"),
    MANAGER_NAME("managerName"), AUTHORIZATION_HEADER("authorizationHeader"), IS_ADMIN("isAdmin"),
    IS_MANAGER("isManager"), BODY_REQUEST("bodyRequest"), ST_TOKEN("stToken");

    private final String value;

    TracingKeyword(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
