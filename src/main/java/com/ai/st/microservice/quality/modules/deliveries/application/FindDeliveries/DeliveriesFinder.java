package com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;

import com.ai.st.microservice.quality.modules.shared.application.PageableResponse;
import com.ai.st.microservice.quality.modules.shared.domain.PageableDomain;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.criteria.*;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ErrorFromInfrastructure;

import java.util.*;
import java.util.stream.Collectors;

@Service
public final class DeliveriesFinder {

    private final DeliveryRepository repository;

    private final static int PAGE_DEFAULT = 1;
    private final static int LIMIT_DEFAULT = 10;

    public DeliveriesFinder(DeliveryRepository repository) {
        this.repository = repository;
    }

    public PageableResponse<DeliveryResponse> finder(DeliveriesFinderQuery query) {

        List<Filter> filters = new ArrayList<>(
                Collections.singletonList(filterByRole(query.role(), query.entityCode())));

        if (query.states().size() > 0) {
            filters.add(filterByStatus(query.states(), query.role()));
        } else {
            List<Long> defaultStatuses = Arrays.asList(
                    DeliveryStatusId.DRAFT,
                    DeliveryStatusId.DELIVERED,
                    DeliveryStatusId.IN_VALIDATION,
                    DeliveryStatusId.ACCEPTED,
                    DeliveryStatusId.REJECTED
            );
            filters.add(filterByStatus(defaultStatuses, query.role()));
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
        return (page <= 0) ? PAGE_DEFAULT : page;
    }

    private int verifyLimit(int limit) {
        return (limit <= 9) ? LIMIT_DEFAULT : limit;
    }

    private Filter filterByRole(Roles role, Long entityCode) {
        FilterField filterField = (role.equals(Roles.OPERATOR)) ? new FilterField("operator") :
                new FilterField("manager");
        return new Filter(filterField, FilterOperator.EQUAL, new FilterValue(entityCode.toString()));
    }

    private Filter filterByStatus(List<Long> states, Roles role) {

        List<DeliveryStatusId> statusesAllowed = new ArrayList<>(Arrays.asList(
                new DeliveryStatusId(DeliveryStatusId.DELIVERED),
                new DeliveryStatusId(DeliveryStatusId.IN_VALIDATION),
                new DeliveryStatusId(DeliveryStatusId.ACCEPTED),
                new DeliveryStatusId(DeliveryStatusId.REJECTED)
        ));

        if (role.equals(Roles.OPERATOR)) {
            statusesAllowed.add(new DeliveryStatusId(DeliveryStatusId.DRAFT));
        }

        List<Long> statusesApproved = filterStatuses(states, statusesAllowed);

        return new Filter(
                new FilterField("deliveryStatus"), FilterOperator.CONTAINS,
                statusesApproved.stream().
                        map(stateId -> new FilterValue(stateId.toString())).collect(Collectors.toList()));
    }

    private List<Long> filterStatuses(List<Long> statuses, List<DeliveryStatusId> allows) {
        for (Long stateId : statuses) {
            DeliveryStatusId deliveryStatusIdFound = allows.stream()
                    .filter(deliveryStatusId -> deliveryStatusId.value().equals(stateId)).findAny().orElse(null);
            if (deliveryStatusIdFound == null) {
                statuses.remove(stateId);
            }
        }
        return statuses;
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
