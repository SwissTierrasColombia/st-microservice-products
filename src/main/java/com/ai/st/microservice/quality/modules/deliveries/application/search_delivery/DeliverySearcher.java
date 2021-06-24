package com.ai.st.microservice.quality.modules.deliveries.application.search_delivery;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliverySearcher implements QueryUseCase<DeliverySearcherQuery, DeliveryResponse> {

    private final DeliveryRepository repository;

    public DeliverySearcher(DeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeliveryResponse handle(DeliverySearcherQuery query) {

        Delivery delivery = repository.search(new DeliveryId(query.deliveryId()));

        verifyDelivery(delivery);

        verifyEntityBelongToDelivery(delivery, query.role(), query.entityCode());

        verifyDeliveryState(delivery, query.role());

        return DeliveryResponse.fromAggregate(delivery);
    }

    private void verifyDelivery(Delivery delivery) {
        if (delivery == null)
            throw new DeliveryNotFound();
    }

    private void verifyEntityBelongToDelivery(Delivery delivery, Roles role, Long entityCode) {
        if (role == Roles.MANAGER && !delivery.deliveryBelongToManager(new ManagerCode(entityCode))) {
            throw new UnauthorizedToSearchDelivery();
        }
        if (role == Roles.OPERATOR && !delivery.deliveryBelongToOperator(new OperatorCode(entityCode))) {
            throw new UnauthorizedToSearchDelivery();
        }
    }

    private void verifyDeliveryState(Delivery delivery, Roles role) {
        if (role.equals(Roles.MANAGER) && !delivery.isAvailableToManager()) {
            throw new UnauthorizedToSearchDelivery();
        }
    }

}
