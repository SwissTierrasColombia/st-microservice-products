package com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryProductStatuses;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductStatusResponse;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductStatusRepository;
import com.ai.st.microservice.quality.modules.shared.application.ListResponse;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class DeliveryProductStatusesFinder implements QueryUseCase<DeliveryProductStatusesFinderQuery, ListResponse<DeliveryProductStatusResponse>> {


    private final DeliveryProductStatusRepository repository;

    public DeliveryProductStatusesFinder(DeliveryProductStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public ListResponse<DeliveryProductStatusResponse> handle(DeliveryProductStatusesFinderQuery query) {
        return new ListResponse<>(repository.all().stream().map(DeliveryProductStatusResponse::fromAggregate).collect(Collectors.toList()));
    }

}
