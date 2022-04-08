package com.ai.st.microservice.quality.modules.products.application.find_products_from_manager;

import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.application.ListResponse;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class ManagerProductsFinder
        implements QueryUseCase<ManagerProductsFinderQuery, ListResponse<ProductResponse>> {

    private final ProductRepository repository;

    public ManagerProductsFinder(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ListResponse<ProductResponse> handle(ManagerProductsFinderQuery query) {

        ManagerCode managerCode = ManagerCode.fromValue(query.managerCode());

        List<Product> products = repository.findProductsByManager(managerCode);

        return new ListResponse<>(products.stream().map(ProductResponse::fromAggregate).collect(Collectors.toList()));
    }

}
