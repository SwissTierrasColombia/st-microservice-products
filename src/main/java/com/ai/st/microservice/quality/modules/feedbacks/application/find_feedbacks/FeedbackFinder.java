package com.ai.st.microservice.quality.modules.feedbacks.application.find_feedbacks;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.feedbacks.application.FeedbackResponse;
import com.ai.st.microservice.quality.modules.feedbacks.domain.contracts.DeliveryProductFeedbackRepository;
import com.ai.st.microservice.quality.modules.shared.application.ListResponse;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class FeedbackFinder implements QueryUseCase<FeedbackFinderQuery, ListResponse<FeedbackResponse>> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductFeedbackRepository feedbackRepository;

    public FeedbackFinder(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
                          DeliveryProductFeedbackRepository feedbackRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public ListResponse<FeedbackResponse> handle(FeedbackFinderQuery query) {

        DeliveryId deliveryId = DeliveryId.fromValue(query.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(query.deliveryProductId());

        verifyPermissions(deliveryId, deliveryProductId, query.role(), query.entityCode());

        return new ListResponse<>(
                feedbackRepository.findByDeliveryProductId(deliveryProductId).stream().map(FeedbackResponse::fromAggregate).collect(Collectors.toList()));
    }

    private void verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId, Roles role, Long entityCode) {

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
            if (!delivery.deliveryBelongToManager(ManagerCode.fromValue(entityCode)) || !delivery.isAvailableToManager()) {
                throw new UnauthorizedToSearchDelivery();
            }
        }

    }

}
