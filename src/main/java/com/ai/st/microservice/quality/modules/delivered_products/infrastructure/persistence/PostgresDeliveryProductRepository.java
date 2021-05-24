package com.ai.st.microservice.quality.modules.delivered_products.infrastructure.persistence;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.infrastructure.persistence.jpa.DeliveryProductJPARepository;

import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveredProductEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveryEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.DeliveryProductStatusEntity;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.ProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class PostgresDeliveryProductRepository implements DeliveryProductRepository {

    private final DeliveryProductJPARepository repository;

    public PostgresDeliveryProductRepository(DeliveryProductJPARepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DeliveryProduct> findByDeliveryId(DeliveryId deliveryId) {
        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setId(deliveryId.value());
        return repository.findByDelivery(deliveryEntity).stream().map(this::mapping).collect(Collectors.toList());
    }

    @Override
    public void save(DeliveryId deliveryId, DeliveryProduct deliveryProduct) {

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(deliveryProduct.productId().value());

        DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setId(deliveryId.value());

        DeliveryProductStatusEntity statusEntity = new DeliveryProductStatusEntity();
        statusEntity.setId(deliveryProduct.deliveryProductStatusId().value());

        DeliveredProductEntity deliveredProductEntity = new DeliveredProductEntity();
        deliveredProductEntity.setCreatedAt(deliveryProduct.deliveryProductDate().value());
        deliveredProductEntity.setProduct(productEntity);
        deliveredProductEntity.setDelivery(deliveryEntity);
        deliveredProductEntity.setStatus(statusEntity);
        deliveredProductEntity.setObservations(deliveryProduct.deliveryProductObservations().value());

        repository.save(deliveredProductEntity);
    }

    @Override
    public DeliveryProduct search(DeliveryProductId deliveryProductId) {
        DeliveredProductEntity deliveredProductEntity = repository.findById(deliveryProductId.value()).orElse(null);
        return (deliveredProductEntity == null) ? null : mapping(deliveredProductEntity);
    }

    @Override
    public void remove(DeliveryProductId deliveryProductId) {
        repository.deleteById(deliveryProductId.value());
    }

    @Override
    public void update(DeliveryProduct deliveryProduct) {
        DeliveredProductEntity deliveredProductEntity = repository.findById(deliveryProduct.deliveryProductId().value()).orElse(null);
        if (deliveredProductEntity != null) {
            deliveredProductEntity.setObservations(deliveryProduct.deliveryProductObservations().value());
            repository.save(deliveredProductEntity);
        }
    }

    @Override
    public List<DeliveryProduct> findByProductId(ProductId productId) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId.value());
        return repository.findByProduct(productEntity).stream().map(this::mapping).collect(Collectors.toList());
    }

    @Override
    public void changeStatus(DeliveryProductId deliveryProductId, DeliveryProductStatusId deliveryProductStatusId) {
        DeliveredProductEntity deliveredProductEntity = repository.findById(deliveryProductId.value()).orElse(null);
        if (deliveredProductEntity != null) {
            DeliveryProductStatusEntity statusEntity = new DeliveryProductStatusEntity();
            statusEntity.setId(deliveryProductStatusId.value());
            deliveredProductEntity.setStatus(statusEntity);
            repository.save(deliveredProductEntity);
        }
    }

    private DeliveryProduct mapping(DeliveredProductEntity deliveredProductEntity) {
        return DeliveryProduct.fromPrimitives(
                deliveredProductEntity.getId(),
                deliveredProductEntity.getCreatedAt(),
                deliveredProductEntity.getObservations(),
                deliveredProductEntity.getProduct().getId(),
                deliveredProductEntity.getStatus().getId());
    }

}
