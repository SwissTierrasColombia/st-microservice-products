package com.ai.st.microservice.quality.modules.feedbacks.infrastructure.persistence;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;
import com.ai.st.microservice.quality.modules.feedbacks.domain.contracts.DeliveryProductFeedbackRepository;
import com.ai.st.microservice.quality.modules.feedbacks.infrastructure.persistence.jpa.DeliveredProductFeedbackJPARepository;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductFeedbackEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PostgresDeliveryProductFeedbackRepository implements DeliveryProductFeedbackRepository {

    private final DeliveredProductFeedbackJPARepository repository;

    public PostgresDeliveryProductFeedbackRepository(DeliveredProductFeedbackJPARepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(DeliveryProductId deliveryProductId, Feedback feedback) {

        DeliveredProductEntity deliveredProductEntity = new DeliveredProductEntity();
        deliveredProductEntity.setId(deliveryProductId.value());

        DeliveredProductFeedbackEntity feedbackEntity = new DeliveredProductFeedbackEntity();
        feedbackEntity.setFeedback(feedback.comments().value());
        feedbackEntity.setCreatedAt(feedback.date().value());
        if (feedback.urlAttachment() != null) {
            feedbackEntity.setAttachmentUrl(feedback.urlAttachment().value());
        }
        feedbackEntity.setDeliveredProduct(deliveredProductEntity);

        repository.save(feedbackEntity);
    }

    @Override
    public List<Feedback> findByDeliveryProductId(DeliveryProductId deliveryProductId) {

        DeliveredProductEntity deliveredProductEntity = new DeliveredProductEntity();
        deliveredProductEntity.setId(deliveryProductId.value());

        return repository.findByDeliveredProduct(deliveredProductEntity).stream().map(this::mapping).collect(Collectors.toList());
    }

    private Feedback mapping(DeliveredProductFeedbackEntity entity) {
        return Feedback.fromPrimitives(
                entity.getId(),
                entity.getFeedback(),
                entity.getAttachmentUrl(),
                entity.getCreatedAt()
        );
    }

}
