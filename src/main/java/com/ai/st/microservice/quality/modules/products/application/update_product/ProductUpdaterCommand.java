package com.ai.st.microservice.quality.modules.products.application.update_product;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class ProductUpdaterCommand implements Command {

    private final Long productId;
    private final String name;
    private final String description;
    private final boolean isXTF;
    private final Long managerCode;

    public ProductUpdaterCommand(Long productId, String name, String description, boolean isXTF, Long managerCode) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.isXTF = isXTF;
        this.managerCode = managerCode;
    }

    public Long productId() {
        return productId;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean isXTF() {
        return isXTF;
    }

    public Long managerCode() {
        return managerCode;
    }
}
