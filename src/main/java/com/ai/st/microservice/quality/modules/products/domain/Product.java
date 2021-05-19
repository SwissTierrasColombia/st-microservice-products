package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;

import java.util.Date;
import java.util.Objects;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;
    private final ManagerCode managerCode;
    private final ProductXTF productXTF;
    private final Date createdAt;

    public Product(ProductId id, ProductName name, ProductDescription description, ManagerCode managerCode, ProductXTF productXTF, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerCode = managerCode;
        this.productXTF = productXTF;
        this.createdAt = createdAt;
    }

    public static Product fromPrimitives(Long id, String name, String description, Long managerCode, boolean productXTF, Date createdAt) {
        return new Product(
                new ProductId(id),
                new ProductName(name),
                new ProductDescription(description),
                new ManagerCode(managerCode),
                new ProductXTF(productXTF),
                createdAt);
    }

    public ProductId id() {
        return id;
    }

    public ProductName name() {
        return name;
    }

    public ProductDescription description() {
        return description;
    }

    public ManagerCode managerCode() {
        return managerCode;
    }

    public ProductXTF productXTF() {
        return productXTF;
    }

    public Date createdAt() {
        return createdAt;
    }

    public boolean isConfiguredAsXTF() {
        return productXTF().value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) && name.equals(product.name)
                && description.equals(product.description)
                && managerCode.equals(product.managerCode)
                && productXTF.equals(product.productXTF) && createdAt.equals(product.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, managerCode, productXTF, createdAt);
    }
}
