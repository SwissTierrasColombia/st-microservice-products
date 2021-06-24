package com.ai.st.microservice.quality.modules.products.application.create_product;

import com.ai.st.microservice.quality.modules.products.domain.*;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;

@Service
public final class ProductCreator implements CommandUseCase<ProductCreatorCommand> {

    private final ProductRepository repository;
    private final DateTime dateTime;

    public ProductCreator(ProductRepository productRepository, DateTime dateTime) {
        this.repository = productRepository;
        this.dateTime = dateTime;
    }

    @Override
    public void handle(ProductCreatorCommand command) {

        ProductName productName = ProductName.fromValue(command.name());
        ProductDescription productDescription = ProductDescription.fromValue(command.description());
        ProductXTF productXTF = ProductXTF.fromValue(command.isXTF());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        Product product = Product.create(
                productName,
                productDescription,
                managerCode,
                productXTF,
                ProductDate.fromValue(dateTime.now())
        );

        repository.save(product);
    }

}
