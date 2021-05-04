package com.ai.st.microservice.quality.modules.products.infrastructure.persistence.jpa.repositories;

import com.ai.st.microservice.quality.modules.products.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductJPARepository extends CrudRepository<ProductEntity, Long> {

    List<ProductEntity> findByManagerCode(Long managerCode);

}
