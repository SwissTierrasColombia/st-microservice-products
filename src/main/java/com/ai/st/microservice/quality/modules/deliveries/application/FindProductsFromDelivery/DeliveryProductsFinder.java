package com.ai.st.microservice.quality.modules.deliveries.application.FindProductsFromDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.DeliverySearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.DeliverySearcherQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class DeliveryProductsFinder {

    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliverySearcher deliverySearcher;

    public DeliveryProductsFinder(DeliveryProductRepository deliveryProductRepository, DeliveryRepository deliveryRepository) {
        this.deliveryProductRepository = deliveryProductRepository;
        this.deliverySearcher = new DeliverySearcher(deliveryRepository);
    }

    public List<DeliveryProductResponse> finder(DeliveryProductsFinderQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        Roles role = query.role();
        Long entityCode = query.entityCode();

        DeliveryResponse deliveryResponse = verifyDeliveryExists(deliveryId.value(), role, entityCode);
        Delivery delivery = createDeliveryFromResponse(deliveryResponse);

        verifyEntityBelongToDelivery(delivery, role, entityCode);
        verifyDeliveryState(delivery, role);

        return deliveryProductRepository.findProductsFromDelivery(deliveryId).stream()
                .map(DeliveryProductResponse::fromAggregate).collect(Collectors.toList());
    }

    private DeliveryResponse verifyDeliveryExists(Long deliveryId, Roles role, Long entityCode) {
        return deliverySearcher.search(
                new DeliverySearcherQuery(deliveryId, role, entityCode)
        );
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

    private Delivery createDeliveryFromResponse(DeliveryResponse deliveryResponse) {
        return Delivery.fromPrimitives(deliveryResponse.id(), deliveryResponse.code(), deliveryResponse.municipalityCode(),
                deliveryResponse.managerCode(), deliveryResponse.operatorCode(), deliveryResponse.userCode(),
                deliveryResponse.observations(), deliveryResponse.deliveryDate(), deliveryResponse.deliveryStatusId());
    }

}
