package com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.DeliveryJPARepository;
import com.ai.st.microservice.quality.modules.delivered_products.infrastructure.persistence.jpa.DeliveryProductStatusJPARepository;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.DeliveryStatusJPARepository;
import com.ai.st.microservice.quality.modules.products.infrastructure.persistence.jpa.ProductJPARepository;
import com.ai.st.microservice.quality.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.quality.modules.shared.domain.criteria.*;
import com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public final class PostgresDeliveryRepository implements DeliveryRepository {

    private final DeliveryStatusJPARepository deliveryStatusJPARepository;
    private final DeliveryProductStatusJPARepository deliveryProductStatusJPARepository;
    private final ProductJPARepository productJPARepository;
    private final DeliveryJPARepository deliveryJPARepository;

    public static final Map<String, String> MAPPING_FIELDS = new HashMap<>(
            Map.ofEntries(
                    new AbstractMap.SimpleEntry<>("deliveryDate", "createdAt"),
                    new AbstractMap.SimpleEntry<>("deliveryStatus", "deliveryStatus"),
                    new AbstractMap.SimpleEntry<>("manager", "managerCode"),
                    new AbstractMap.SimpleEntry<>("operator", "operatorCode"),
                    new AbstractMap.SimpleEntry<>("code", "code"),
                    new AbstractMap.SimpleEntry<>("municipality", "municipalityCode")
            ));

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

    @Override
    public PageableDomain<Delivery> matching(Criteria criteria) {

        List<DeliveryEntity> deliveryEntities;
        Page<DeliveryEntity> page = null;

        if (criteria.hasFilters()) {

            Specification<DeliveryEntity> specification = addFilters(criteria.filters());

            if (criteria.hasPagination()) {
                Pageable pageable = addPageable(criteria.page().get(), criteria.limit().get(), criteria.order());
                page = deliveryJPARepository.findAll(specification, pageable);
                deliveryEntities = page.getContent();
            } else {
                if (criteria.order().hasOrder()) {
                    Sort sort = addSorting(criteria.order().orderBy(), criteria.order().orderType());
                    deliveryEntities = deliveryJPARepository.findAll(specification, sort);
                } else {
                    deliveryEntities = deliveryJPARepository.findAll(specification);
                }
            }

        } else {
            if (criteria.hasPagination()) {
                Pageable pageable = addPageable(criteria.page().get(), criteria.limit().get(), criteria.order());
                page = deliveryJPARepository.findAll(pageable);
                deliveryEntities = page.getContent();
            } else {
                if (criteria.order().hasOrder()) {
                    Sort sort = addSorting(criteria.order().orderBy(), criteria.order().orderType());
                    deliveryEntities = deliveryJPARepository.findAll(sort);
                } else {
                    deliveryEntities = deliveryJPARepository.findAll();
                }
            }
        }

        List<Delivery> deliveries = deliveryEntities.stream().map(deliveryEntity -> Delivery.fromPrimitives(
                deliveryEntity.getId(), deliveryEntity.getCode(), deliveryEntity.getMunicipalityCode(),
                deliveryEntity.getManagerCode(), deliveryEntity.getOperatorCode(), deliveryEntity.getUserCode(),
                deliveryEntity.getObservations(), deliveryEntity.getCreatedAt(), deliveryEntity.getDeliveryStatus().getId()
        )).collect(Collectors.toList());

        return new PageableDomain<>(
                deliveries,
                page != null ? Optional.of(page.getNumber() + 1) : Optional.empty(),
                page != null ? Optional.of(page.getNumberOfElements()) : Optional.empty(),
                page != null ? Optional.of(page.getTotalElements()) : Optional.empty(),
                page != null ? Optional.of(page.getTotalPages()) : Optional.empty(),
                page != null ? Optional.of(page.getSize()) : Optional.empty()
        );
    }

    private Specification<DeliveryEntity> addFilters(List<Filter> filters) {
        Specification<DeliveryEntity> specification = where(createSpecification(filters.remove(0)));
        for (Filter filter : filters) {
            specification = specification != null ? specification.and(createSpecification(filter)) : null;
        }
        return specification;
    }

    private Sort addSorting(OrderBy orderBy, OrderType orderType) {
        Sort sort = Sort.by(mappingField(orderBy.value()));
        sort = (orderType.isAsc()) ? sort.ascending() : sort.descending();
        return sort;
    }

    private Pageable addPageable(int page, int limit, Order order) {
        Pageable pageable;
        int numberPage = page - 1;
        if (order.hasOrder()) {
            Sort sort = addSorting(order.orderBy(), order.orderType());
            pageable = PageRequest.of(numberPage, limit, sort);
        } else {
            pageable = PageRequest.of(numberPage, limit);
        }
        return pageable;
    }

    private Specification<DeliveryEntity> createSpecification(Filter filter) {
        try {
            switch (filter.operator()) {
                case EQUAL:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(buildPath(root, filter.field().value()), filter.value().value());
                case NOT_EQUAL:
                    return (root, query, criteriaBuilder) ->
                            criteriaBuilder.notEqual(buildPath(root, filter.field().value()), filter.value().value());
                case CONTAINS:
                    return (root, query, criteriaBuilder) -> {
                        List<String> list = filter.values().stream().map(FilterValue::value).collect(Collectors.toList());
                        return buildPath(root, filter.field().value()).in(list);
                    };
                default:
                    throw new OperatorUnsupported();
            }
        } catch (Exception e) {
            throw new FieldUnsupported();
        }
    }

    private Path<?> buildPath(Root<DeliveryEntity> root, String fieldDomain) {
        String field = mappingField(fieldDomain);
        if (root.get(field).getJavaType().isAssignableFrom(DeliveryStatusEntity.class)) {
            return root.get(field).get("id");
        }
        return root.get(field);
    }

    private String mappingField(String fieldDomain) {
        return MAPPING_FIELDS.get(fieldDomain);
    }

    @Override
    public void remove(DeliveryId deliveryId) {
        deliveryJPARepository.deleteById(deliveryId.value());
    }

    @Override
    public void update(Delivery delivery) {
        DeliveryEntity deliveryEntity = deliveryJPARepository.findById(delivery.id().value()).orElse(null);
        if (deliveryEntity != null) {
            deliveryEntity.setObservations(delivery.observations().value());
            deliveryJPARepository.save(deliveryEntity);
        }
    }

    @Override
    public void changeState(DeliveryId deliveryId, DeliveryStatusId deliveryStatusId) {
        DeliveryEntity deliveryEntity = deliveryJPARepository.findById(deliveryId.value()).orElse(null);
        if (deliveryEntity != null) {
            DeliveryStatusEntity deliveryStatusEntity = new DeliveryStatusEntity();
            deliveryStatusEntity.setId(deliveryStatusId.value());

            deliveryEntity.setDeliveryStatus(deliveryStatusEntity);
            deliveryJPARepository.save(deliveryEntity);
        }
    }

}
