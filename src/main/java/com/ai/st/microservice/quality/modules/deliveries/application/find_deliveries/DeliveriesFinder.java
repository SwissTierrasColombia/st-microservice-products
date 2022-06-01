package com.ai.st.microservice.quality.modules.deliveries.application.find_deliveries;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryCode;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;

import com.ai.st.microservice.quality.modules.shared.application.PageableResponse;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.*;
import com.ai.st.microservice.quality.modules.shared.domain.criteria.*;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ErrorFromInfrastructure;

import java.util.*;
import java.util.stream.Collectors;

@Service
public final class DeliveriesFinder implements QueryUseCase<DeliveriesFinderQuery, PageableResponse<DeliveryResponse>> {

    private final DeliveryRepository repository;

    private final static int PAGE_DEFAULT = 1;
    private final static int LIMIT_DEFAULT = 10;

    public DeliveriesFinder(DeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageableResponse<DeliveryResponse> handle(DeliveriesFinderQuery query) {

        List<Filter> filters = new ArrayList<>(
                Collections.singletonList(filterByRole(query.role(), query.entityCode())));

        if (query.states().size() > 0) {
            filters.add(filterByStatus(query.states(), query.role()));
        } else {
            List<Long> defaultStatuses = Arrays.asList(DeliveryStatusId.DRAFT, DeliveryStatusId.DELIVERED,
                    DeliveryStatusId.IN_REVIEW, DeliveryStatusId.IN_REMEDIATION, DeliveryStatusId.ACCEPTED,
                    DeliveryStatusId.REJECTED);
            filters.add(filterByStatus(defaultStatuses, query.role()));
        }

        String code = query.code();
        if (code != null && !code.isEmpty()) {
            filters.add(filterByCode(code));
        }

        String municipality = query.municipality();
        if (municipality != null && !municipality.isEmpty()) {
            filters.add(filterByMunicipality(municipality));
        }

        Long operator = query.operator();
        if (operator != null && !query.role().equals(Roles.OPERATOR)) {
            filters.add(filterByOperator(operator));
        }

        Long manager = query.manager();
        if (manager != null && !query.role().equals(Roles.MANAGER)) {
            filters.add(filterByManager(manager));
        }

        Criteria criteria = new Criteria(filters, Order.fromValues(Optional.of("deliveryDate"), Optional.of("DESC")),
                Optional.of(verifyPage(query.page())), Optional.of(verifyLimit(query.limit())));

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
        FilterField filterField = (role.equals(Roles.OPERATOR)) ? new FilterField("operator")
                : new FilterField("manager");
        return new Filter(filterField, FilterOperator.EQUAL, new FilterValue(entityCode.toString()));
    }

    private Filter filterByStatus(List<Long> statuses, Roles role) {

        List<DeliveryStatusId> statusesAllowed = (role.equals(Roles.OPERATOR)) ? Delivery.statusesAllowedToOperator()
                : Delivery.statusesAllowedToManager();

        List<Long> statusesApproved = filterStatuses(statuses, statusesAllowed);

        return new Filter(new FilterField("deliveryStatus"), FilterOperator.CONTAINS, statusesApproved.stream()
                .map(stateId -> new FilterValue(stateId.toString())).collect(Collectors.toList()));
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

    private Filter filterByCode(String code) {
        return new Filter(new FilterField("code"), FilterOperator.EQUAL,
                new FilterValue(DeliveryCode.fromValue(code).value()));
    }

    private Filter filterByMunicipality(String municipality) {
        return new Filter(new FilterField("municipality"), FilterOperator.EQUAL,
                new FilterValue(MunicipalityCode.fromValue(municipality).value()));
    }

    private Filter filterByOperator(Long operator) {
        return new Filter(new FilterField("operator"), FilterOperator.EQUAL,
                new FilterValue(OperatorCode.fromValue(operator).value().toString()));
    }

    private Filter filterByManager(Long manager) {
        return new Filter(new FilterField("manager"), FilterOperator.EQUAL,
                new FilterValue(ManagerCode.fromValue(manager).value().toString()));
    }

    private PageableResponse<DeliveryResponse> buildResponse(PageableDomain<Delivery> pageableDomain) {

        if (!pageableDomain.numberOfElements().isPresent() || !pageableDomain.totalElements().isPresent()
                || !pageableDomain.totalPages().isPresent() || !pageableDomain.size().isPresent()
                || !pageableDomain.currentPage().isPresent()) {
            throw new ErrorFromInfrastructure();
        }

        List<DeliveryResponse> deliveriesResponse = pageableDomain.items().stream().map(DeliveryResponse::fromAggregate)
                .collect(Collectors.toList());

        return new PageableResponse<>(deliveriesResponse, pageableDomain.currentPage().get(),
                pageableDomain.numberOfElements().get(), pageableDomain.totalElements().get(),
                pageableDomain.totalPages().get(), pageableDomain.size().get());
    }

}
