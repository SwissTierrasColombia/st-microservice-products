package com.ai.st.microservice.quality.modules.products.application.FindProductsFromManager;

import com.ai.st.microservice.quality.modules.shared.application.Query;

public final class ManagerProductsFinderQuery implements Query {

    private final Long managerCode;

    public ManagerProductsFinderQuery(Long managerCode) {
        this.managerCode = managerCode;
    }

    public Long managerCode() {
        return managerCode;
    }

}
