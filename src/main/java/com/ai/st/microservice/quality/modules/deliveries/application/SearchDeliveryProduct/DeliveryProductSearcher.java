package com.ai.st.microservice.quality.modules.deliveries.application.SearchDeliveryProduct;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductId;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

@Service
public final class DeliveryProductSearcher implements QueryUseCase<DeliveryProductSearcherQuery, DeliveryProductResponse> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;

    public DeliveryProductSearcher(DeliveryProductRepository deliveryProductRepository, DeliveryRepository deliveryRepository) {
        this.deliveryProductRepository = deliveryProductRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public DeliveryProductResponse handle(DeliveryProductSearcherQuery query) {

        DeliveryId deliveryId = DeliveryId.fromValue(query.deliveryId());
        DeliveryProductId deliveryProductId = new DeliveryProductId(query.deliveryProductId());

        DeliveryProduct deliveryProduct = deliveryProductRepository.search(deliveryProductId);

        verifyDeliveryProduct(deliveryProduct);

        verifyPermissions(deliveryId, query.role(), query.entityCode());

        return DeliveryProductResponse.fromAggregate(deliveryProduct);
    }

    private void verifyDeliveryProduct(DeliveryProduct deliveryProduct) {
        if (deliveryProduct == null)
            throw new DeliveryProductNotFound();
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
