package com.ai.st.microservice.quality.modules.products.application;

import com.ai.st.microservice.quality.modules.products.domain.Product;

import java.util.Date;

public final class ProductResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final Long managerCode;
    private final boolean isXTF;
    private final Date createdAt;

    public ProductResponse(Long id, String name, String description, Long managerCode, boolean method, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerCode = managerCode;
        this.isXTF = method;
        this.createdAt = createdAt;
    }

    public static ProductResponse fromAggregate(Product product) {
        return new ProductResponse(product.id().value(), product.name().value(), product.description().value(),
                product.managerCode().value(), product.productXTF().value(), product.productDate().value());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Long managerCode() {
        return managerCode;
    }

    public boolean xtf() {
        return isXTF;
    }

    public Date createdAt() {
        return createdAt;
    }
}
