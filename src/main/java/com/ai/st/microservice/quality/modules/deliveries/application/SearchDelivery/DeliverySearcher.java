package com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliverySearcher {

    private final DeliveryRepository repository;

    public DeliverySearcher(DeliveryRepository repository) {
        this.repository = repository;
    }

    public DeliveryResponse search(SearchDeliveryQuery query) {

        Delivery delivery = repository.search(new DeliveryId(query.deliveryId()));

        verifyDelivery(delivery);
        verifyEntityBelongToDelivery(delivery, query.role(), query.entityCode());

        return DeliveryResponse.fromAggregate(delivery);
    }

    private void verifyDelivery(Delivery delivery) {
        if (delivery == null)
            throw new DeliveryNotFound();
    }

    private void verifyEntityBelongToDelivery(Delivery delivery, Roles role, Long entityCode) {
        if ((role == Roles.MANAGER && !delivery.manager().value().equals(entityCode))
                || (role == Roles.OPERATOR && !delivery.operator().value().equals(entityCode))) {
            throw new UnauthorizedToSearchDelivery();
        }
    }

}
