package com.ai.st.microservice.quality.modules.products.application.FindProductsFromManager;

public final class ManagerProductsFinderQuery {

    private final Long managerCode;

    public ManagerProductsFinderQuery(Long managerCode) {
        this.managerCode = managerCode;
    }

    public Long managerCode() {
        return managerCode;
    }

}
