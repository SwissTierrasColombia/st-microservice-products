package com.ai.st.microservice.quality.modules.products.application.CreateProduct;

public final class ProductCreatorCommand {

    private final String name;
    private final String description;
    private final Long managerCode;
    private final boolean isXTF;

    public ProductCreatorCommand(String name, String description, boolean isXTF, Long managerCode) {
        this.name = name;
        this.description = description;
        this.managerCode = managerCode;
        this.isXTF = isXTF;
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

    public boolean isXTF() {
        return isXTF;
    }

}
