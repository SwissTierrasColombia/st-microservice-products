package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveredProductEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.DeliveryEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveryProductJPARepository extends CrudRepository<DeliveredProductEntity, Long> {

    List<DeliveredProductEntity> findByDelivery(DeliveryEntity deliveryEntity);

    List<DeliveredProductEntity> findByProduct(ProductEntity productEntity);

}
