package com.ai.st.microservice.quality.modules.products.application.RemoveProduct;

import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductDoesNotBelongToManager;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.ProductNotFound;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.UnauthorizedToRemoveProduct;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class ProductRemover {

    private final ProductRepository productRepository;
    private final DeliveryProductRepository deliveryProductRepository;

    public ProductRemover(ProductRepository repository, DeliveryProductRepository deliveryProductRepository) {
        this.productRepository = repository;
        this.deliveryProductRepository = deliveryProductRepository;
    }

    public void remove(ProductRemoverCommand command) {

        ProductId productId = ProductId.fromValue(command.productId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        Product product = productRepository.search(productId);
        checkProduct(product);

        verifyPermissions(product, managerCode);

        productRepository.remove(productId);
    }

    private void checkProduct(Product product) {
        if (product == null)
            throw new ProductNotFound();
    }

    private void verifyPermissions(Product product, ManagerCode managerCode) {

        if (!product.belongToManager(managerCode)) {
            throw new ProductDoesNotBelongToManager();
        }

        long count = deliveryProductRepository.findByProductId(product.id()).size();
        if (count > 0) {
            throw new UnauthorizedToRemoveProduct("No se puede eliminar el producto porque hace parte de una entrega.");
        }

    }

}
