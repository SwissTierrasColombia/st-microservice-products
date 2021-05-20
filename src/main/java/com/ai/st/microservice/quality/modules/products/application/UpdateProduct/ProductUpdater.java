package com.ai.st.microservice.quality.modules.products.application.UpdateProduct;

import com.ai.st.microservice.quality.modules.products.domain.*;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDoesNotBelongToManager;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductNotFound;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class ProductUpdater {

    private final ProductRepository repository;

    public ProductUpdater(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    public void update(ProductUpdaterCommand command) {

        ProductId productId = ProductId.fromValue(command.productId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());
        ProductName productName = ProductName.fromValue(command.name());
        ProductDescription productDescription = ProductDescription.fromValue(command.description());
        ProductXTF productXTF = ProductXTF.fromValue(command.isXTF());

        Product product = repository.search(productId);
        checkProduct(product);

        verifyPermissions(product, managerCode);

        repository.update(Product.create(
                productId,
                productName,
                productDescription,
                managerCode,
                productXTF,
                product.productDate()
        ));
    }

    private void checkProduct(Product product) {
        if (product == null)
            throw new ProductNotFound();
    }

    private void verifyPermissions(Product product, ManagerCode managerCode) {
        if (!product.belongToManager(managerCode))
            throw new ProductDoesNotBelongToManager();
    }

}
