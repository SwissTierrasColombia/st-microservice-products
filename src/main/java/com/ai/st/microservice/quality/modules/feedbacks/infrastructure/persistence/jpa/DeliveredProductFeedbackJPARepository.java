package com.ai.st.microservice.quality.modules.feedbacks.infrastructure.persistence.jpa;

import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductFeedbackEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeliveredProductFeedbackJPARepository extends CrudRepository<DeliveredProductFeedbackEntity, Long> {

    List<DeliveredProductFeedbackEntity> findByDeliveredProduct(DeliveredProductEntity deliveredProductEntity);

}
