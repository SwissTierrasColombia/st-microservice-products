package com.ai.st.microservice.quality.modules.products.infrastructure;

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
        return entities.stream().map(entity -> Product.fromPrimitives(entity.getId(), entity.getName(),
                entity.getDescription(), entity.getManagerCode(), entity.getXTF(), entity.getCreatedAt()))
                .collect(Collectors.toList());
    }
}