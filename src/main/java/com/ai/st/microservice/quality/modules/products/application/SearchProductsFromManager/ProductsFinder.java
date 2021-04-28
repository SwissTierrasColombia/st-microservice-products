package com.ai.st.microservice.quality.modules.products.application.SearchProductsFromManager;

import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class ProductsFinder {

    private final ProductRepository repository;

    public ProductsFinder(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponse> finder(Long managerCode) {

        List<Product> products = repository.findProductsByManager(new ManagerCode(managerCode));

        return products.stream().map(ProductResponse::fromAggregate).collect(Collectors.toList());
    }

}
