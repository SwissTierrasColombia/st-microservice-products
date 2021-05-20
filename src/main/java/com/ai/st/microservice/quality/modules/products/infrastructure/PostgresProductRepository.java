package com.ai.st.microservice.quality.modules.products.infrastructure;

import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.ProductEntity;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.products.infrastructure.persistence.jpa.ProductJPARepository;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PostgresProductRepository implements ProductRepository {

    private final ProductJPARepository repository;

    public PostgresProductRepository(ProductJPARepository productJPARepository) {
        this.repository = productJPARepository;
    }

    @Override
    public List<Product> findProductsByManager(ManagerCode managerCode) {
        List<ProductEntity> entities = repository.findByManagerCode(managerCode.value());
        return entities.stream().map(this::mappingProduct)
                .collect(Collectors.toList());
    }

    @Override
    public Product search(ProductId productId) {
        ProductEntity productEntity = repository.findById(productId.value()).orElse(null);
        return (productEntity == null) ? null : mappingProduct(productEntity);
    }

    @Override
    public void save(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(product.name().value());
        productEntity.setDescription(product.description().value());
        productEntity.setManagerCode(product.managerCode().value());
        productEntity.setXTF(product.productXTF().value());
        productEntity.setCreatedAt(product.productDate().value());

        repository.save(productEntity);
    }

    private Product mappingProduct(ProductEntity entity) {
        return Product.fromPrimitives(entity.getId(), entity.getName(),
                entity.getDescription(), entity.getManagerCode(), entity.getXTF(), entity.getCreatedAt());
    }

}