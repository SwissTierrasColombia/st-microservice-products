package com.ai.st.microservice.quality.modules.deliveries.infrastructure;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.DeliveryJPARepository;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.DeliveryProductStatusJPARepository;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.DeliveryStatusJPARepository;
import com.ai.st.microservice.quality.modules.products.infrastructure.persistence.jpa.ProductJPARepository;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities.*;
import org.springframework.stereotype.Service;

@Service
public final class PostgresDeliveryRepository implements DeliveryRepository {

    private final DeliveryStatusJPARepository deliveryStatusJPARepository;
    private final DeliveryProductStatusJPARepository deliveryProductStatusJPARepository;
    private final ProductJPARepository productJPARepository;
    private final DeliveryJPARepository deliveryJPARepository;

    public PostgresDeliveryRepository(DeliveryStatusJPARepository deliveryStatusJPARepository,
                                      DeliveryProductStatusJPARepository deliveryProductStatusJPARepository,
                                      ProductJPARepository productJPARepository,
                                      DeliveryJPARepository deliveryJPARepository) {
        this.deliveryStatusJPARepository = deliveryStatusJPARepository;
        this.deliveryProductStatusJPARepository = deliveryProductStatusJPARepository;
        this.productJPARepository = productJPARepository;
        this.deliveryJPARepository = deliveryJPARepository;
    }

    @Override
    public void save(Delivery delivery) {

        DeliveryStatusEntity deliveryStatusEntity =
                deliveryStatusJPARepository.findById(delivery.deliveryStatusId().value()).orElse(null);

        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setCreatedAt(delivery.deliveryDate().value());
        deliveryEntity.setManagerCode(delivery.manager().value());
        deliveryEntity.setObservations(delivery.observations().value());
        deliveryEntity.setMunicipalityCode(delivery.municipality().value());
        deliveryEntity.setOperatorCode(delivery.operator().value());
        deliveryEntity.setUserCode(delivery.user().value());
        deliveryEntity.setDeliveryStatus(deliveryStatusEntity);
        deliveryEntity.setCode(delivery.code().value());

        delivery.deliveryProducts().forEach(deliveryProduct -> {
            DeliveryProductStatusEntity deliveryProductStatusEntity =
                    deliveryProductStatusJPARepository.findById(deliveryProduct.deliveryProductStatusId().value()).orElse(null);
            ProductEntity productEntity =
                    productJPARepository.findById(deliveryProduct.productId().value()).orElse(null);
            DeliveredProductEntity deliveredProductEntity = new DeliveredProductEntity();
            deliveredProductEntity.setCreatedAt(deliveryProduct.deliveryProductDate().value());
            deliveredProductEntity.setObservations(deliveryProduct.deliveryProductObservations().value());
            deliveredProductEntity.setDelivery(deliveryEntity);
            deliveredProductEntity.setProduct(productEntity);
            deliveredProductEntity.setStatus(deliveryProductStatusEntity);
            deliveryEntity.getProducts().add(deliveredProductEntity);
        });

        deliveryJPARepository.save(deliveryEntity);
    }

    @Override
    public Delivery search(DeliveryId id) {
        DeliveryEntity deliveryEntity = deliveryJPARepository.findById(id.value()).orElse(null);
        return (deliveryEntity == null) ? null :
                Delivery.fromPrimitives(
                        deliveryEntity.getId(),
                        deliveryEntity.getCode(),
                        deliveryEntity.getMunicipalityCode(),
                        deliveryEntity.getManagerCode(),
                        deliveryEntity.getOperatorCode(),
                        deliveryEntity.getUserCode(),
                        deliveryEntity.getObservations(),
                        deliveryEntity.getCreatedAt(),
                        deliveryEntity.getDeliveryStatus().getId());
    }

}
