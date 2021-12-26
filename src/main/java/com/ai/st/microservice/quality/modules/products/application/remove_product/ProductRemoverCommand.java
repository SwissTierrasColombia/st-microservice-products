package com.ai.st.microservice.quality.modules.products.application.remove_product;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class ProductRemoverCommand implements Command {

    private final Long productId;
    private final Long managerCode;

    public ProductRemoverCommand(Long productId, Long managerCode) {
        this.productId = productId;
        this.managerCode = managerCode;
    }

    public Long productId() {
        return productId;
    }

    public Long managerCode() {
        return managerCode;
    }

}
