package com.ai.st.microservice.quality.modules.products.domain.contracts;

import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;

import java.util.List;

public interface ProductRepository {

    List<Product> findProductsByManager(ManagerCode manager);

    Product search(ProductId productId);

    void save(Product product);

}
