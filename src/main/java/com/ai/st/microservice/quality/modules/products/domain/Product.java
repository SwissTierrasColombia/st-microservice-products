package com.ai.st.microservice.quality.modules.products.domain;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;

import java.util.Date;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;
    private final ManagerCode managerCode;
    private final ProductXTF productXTF;
    private final ProductDate productDate;

    public Product(ProductId id, ProductName name, ProductDescription description, ManagerCode managerCode,
                   ProductXTF productXTF, ProductDate createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerCode = managerCode;
        this.productXTF = productXTF;
        this.productDate = createdAt;
    }

    public static Product create(ProductId productId, ProductName name, ProductDescription description, ManagerCode managerCode,
                                 ProductXTF productXTF, ProductDate createdAt) {
        return new Product(productId, name, description, managerCode, productXTF, createdAt);
    }

    public static Product create(ProductName name, ProductDescription description, ManagerCode managerCode,
                                 ProductXTF productXTF, ProductDate createdAt) {
        return new Product(null, name, description, managerCode, productXTF, createdAt);
    }

    public static Product fromPrimitives(Long id, String name, String description, Long managerCode,
                                         boolean productXTF, Date createdAt) {
        return new Product(
                new ProductId(id),
                new ProductName(name),
                new ProductDescription(description),
                new ManagerCode(managerCode),
                new ProductXTF(productXTF),
                new ProductDate(createdAt));
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

    public ProductDate productDate() {
        return productDate;
    }

    public boolean isConfiguredAsXTF() {
        return productXTF().value();
    }

    public boolean belongToManager(ManagerCode managerCode) {
        return managerCode.value().equals(this.managerCode.value());
    }


}
