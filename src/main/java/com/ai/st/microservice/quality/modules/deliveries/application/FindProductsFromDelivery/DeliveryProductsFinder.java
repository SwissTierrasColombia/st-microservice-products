package com.ai.st.microservice.quality.modules.deliveries.application.FindProductsFromDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class DeliveryProductsFinder {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;

    public DeliveryProductsFinder(DeliveryProductRepository deliveryProductRepository, DeliveryRepository deliveryRepository) {
        this.deliveryProductRepository = deliveryProductRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public List<DeliveryProductResponse> finder(DeliveryProductsFinderQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        Roles role = query.role();
        Long entityCode = query.entityCode();

        verifyPermissions(deliveryId, role, entityCode);

        return deliveryProductRepository.findByDeliveryId(deliveryId).stream()
                .map(DeliveryProductResponse::fromAggregate).collect(Collectors.toList());
    }

    private void verifyPermissions(DeliveryId deliveryId, Roles role, Long entityCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
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
