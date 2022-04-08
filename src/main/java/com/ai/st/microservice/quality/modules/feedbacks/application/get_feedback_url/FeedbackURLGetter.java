package com.ai.st.microservice.quality.modules.feedbacks.application.get_feedback_url;

import com.ai.st.microservice.quality.modules.attachments.domain.exceptions.RemovingAttachmentToProductFailed;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;
import com.ai.st.microservice.quality.modules.feedbacks.domain.FeedbackId;
import com.ai.st.microservice.quality.modules.feedbacks.domain.contracts.DeliveryProductFeedbackRepository;
import com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions.FeedbackDoesNotHaveAttachment;
import com.ai.st.microservice.quality.modules.feedbacks.domain.exceptions.FeedbackNotFound;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.shared.application.StringResponse;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class FeedbackURLGetter implements QueryUseCase<FeedbackURLGetterQuery, StringResponse> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductFeedbackRepository feedbackRepository;

    public FeedbackURLGetter(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
            DeliveryProductFeedbackRepository feedbackRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public StringResponse handle(FeedbackURLGetterQuery query) {

        DeliveryId deliveryId = DeliveryId.fromValue(query.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(query.deliveryProductId());
        FeedbackId feedbackId = FeedbackId.fromValue(query.feedbackId());

        Feedback feedback = verifyPermissions(deliveryId, deliveryProductId, feedbackId, query.role(),
                query.entityCode());

        return new StringResponse(feedback.urlAttachment().value());
    }

    private Feedback verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
            FeedbackId feedbackId, Roles role, Long entityCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify delivery product exists
        DeliveryProduct deliveryProduct = deliveryProductRepository.search(deliveryProductId);
        if (deliveryProduct == null) {
            throw new DeliveryProductNotFound();
        }

        // verify owner of the delivery
        if (role.equals(Roles.OPERATOR)) {
            if (!delivery.deliveryBelongToOperator(OperatorCode.fromValue(entityCode))) {
                throw new UnauthorizedToSearchDelivery();
            }
        }
        if (role.equals(Roles.MANAGER)) {
            // verify status of the delivery
            if (!delivery.deliveryBelongToManager(ManagerCode.fromValue(entityCode))
                    || !delivery.isAvailableToManager()) {
                throw new UnauthorizedToSearchDelivery();
            }
        }

        Feedback feedback = feedbackRepository.search(feedbackId);
        if (feedback == null) {
            throw new FeedbackNotFound();
        }

        // verify attachment belong to delivery product
        if (!feedback.deliveryProductId().value().equals(deliveryProductId.value())) {
            throw new RemovingAttachmentToProductFailed("La retroalimentación no pertenece al producto.");
        }

        if (!feedback.hasAttachment()) {
            throw new FeedbackDoesNotHaveAttachment();
        }

        return feedback;
    }

}
