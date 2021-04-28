package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;

import java.util.Date;
import java.util.Objects;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;
    private final ManagerCode managerCode;
    private final ProductDeliveryMethod method;
    private final Date createdAt;

    public Product(ProductId id, ProductName name, ProductDescription description, ManagerCode managerCode, ProductDeliveryMethod method, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerCode = managerCode;
        this.method = method;
        this.createdAt = createdAt;
    }

    public static Product fromPrimitives(Long id, String name, String description, Long managerCode, String method, Date createdAt) {
        return new Product(
                new ProductId(id),
                new ProductName(name),
                new ProductDescription(description),
                new ManagerCode(managerCode),
                new ProductDeliveryMethod(method),
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

    public ProductDeliveryMethod method() {
        return method;
    }

    public Date createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) && name.equals(product.name)
                && description.equals(product.description)
                && managerCode.equals(product.managerCode)
                && method.equals(product.method) && createdAt.equals(product.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, managerCode, method, createdAt);
    }
}
