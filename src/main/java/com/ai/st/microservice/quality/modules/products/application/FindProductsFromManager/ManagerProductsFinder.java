package com.ai.st.microservice.quality.modules.products.application.FindProductsFromManager;

import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class ManagerProductsFinder {

    private final ProductRepository repository;

    public ManagerProductsFinder(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponse> finder(ManagerProductsFinderQuery query) {

        ManagerCode managerCode = ManagerCode.fromValue(query.managerCode());

        List<Product> products = repository.findProductsByManager(managerCode);

        return products.stream().map(ProductResponse::fromAggregate).collect(Collectors.toList());
    }

}
