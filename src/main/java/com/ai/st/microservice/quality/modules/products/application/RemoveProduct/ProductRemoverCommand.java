package com.ai.st.microservice.quality.modules.products.application.RemoveProduct;

public final class ProductRemoverCommand {

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
