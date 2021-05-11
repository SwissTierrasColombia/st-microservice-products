package com.ai.st.microservice.quality.modules.deliveries.application.FindProductsFromDelivery;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.DeliverySearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchDelivery.SearchDeliveryQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;

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

    public List<DeliveryProductResponse> finder(FindProductsFromDeliveryQuery query) {

        DeliveryId deliveryId = new DeliveryId(query.deliveryId());
        Roles role = query.role();
        Long entityCode = query.entityCode();

        DeliveryResponse deliveryResponse = verifyDeliveryExists(deliveryId, role, entityCode);
        verifyEntityBelongToDelivery(deliveryResponse, role, entityCode);

        return deliveryProductRepository.findProductsFromDelivery(deliveryId).stream()
                .map(DeliveryProductResponse::fromAggregate).collect(Collectors.toList());
    }

    private DeliveryResponse verifyDeliveryExists(DeliveryId deliveryId, Roles role, Long entityCode) {
        return deliverySearcher.search(new SearchDeliveryQuery(
                deliveryId.value(),
                role,
                entityCode
        ));
    }

    private void verifyEntityBelongToDelivery(DeliveryResponse deliveryResponse, Roles role, Long entityCode) {
        if ((role == Roles.MANAGER && !deliveryResponse.managerCode().equals(entityCode))
                || (role == Roles.OPERATOR && !deliveryResponse.operatorCode().equals(entityCode))) {
            throw new UnauthorizedToSearchDelivery();
        }
    }

}
