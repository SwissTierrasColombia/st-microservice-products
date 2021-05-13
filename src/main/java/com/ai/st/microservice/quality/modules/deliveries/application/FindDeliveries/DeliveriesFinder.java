package com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;

import com.ai.st.microservice.quality.modules.shared.application.PageableResponse;
import com.ai.st.microservice.quality.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.criteria.*;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ErrorFromInfrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public final class DeliveriesFinder {

    private final DeliveryRepository repository;

    public DeliveriesFinder(DeliveryRepository repository) {
        this.repository = repository;
    }

    public PageableResponse<DeliveryResponse> finder(DeliveriesFinderQuery query) {

        List<Filter> filters = new ArrayList<>(
                Collections.singletonList(filterByRole(query.role(), query.entityCode())));

        if (query.stateId() != null) {
            filters.add(filterByStatus(query.stateId()));
        }

        Criteria criteria = new Criteria(
                filters,
                Order.fromValues(Optional.of("deliveryDate"), Optional.of("DESC")),
                Optional.of(verifyPage(query.page())),
                Optional.of(verifyLimit(query.limit()))
        );

        PageableDomain<Delivery> pageableDomain = repository.matching(criteria);

        return buildResponse(pageableDomain);
    }

    private int verifyPage(int page) {
        return (page <= 0) ? 1 : page;
    }

    private int verifyLimit(int limit) {
        return (limit <= 9) ? 10 : limit;
    }

    private Filter filterByRole(Roles role, Long entityCode) {
        FilterField filterField = (role.equals(Roles.OPERATOR)) ? new FilterField("operator") :
                new FilterField("manager");
        return new Filter(filterField, FilterOperator.EQUAL, new FilterValue(entityCode.toString()));
    }

    private Filter filterByStatus(Long stateId) {
        return new Filter(
                new FilterField("deliveryStatus"), FilterOperator.EQUAL, new FilterValue(stateId.toString()));
    }

    private PageableResponse<DeliveryResponse> buildResponse(PageableDomain<Delivery> pageableDomain) {

        if (!pageableDomain.numberOfElements().isPresent() || !pageableDomain.totalElements().isPresent()
                || !pageableDomain.totalPages().isPresent() || !pageableDomain.size().isPresent()
                || !pageableDomain.currentPage().isPresent()) {
            throw new ErrorFromInfrastructure();
        }

        List<DeliveryResponse> deliveriesResponse = pageableDomain.items()
                .stream().map(DeliveryResponse::fromAggregate).collect(Collectors.toList());

        return new PageableResponse<>(
                deliveriesResponse,
                pageableDomain.currentPage().get(),
                pageableDomain.numberOfElements().get(),
                pageableDomain.totalElements().get(),
                pageableDomain.totalPages().get(),
                pageableDomain.size().get()
        );
    }

}
