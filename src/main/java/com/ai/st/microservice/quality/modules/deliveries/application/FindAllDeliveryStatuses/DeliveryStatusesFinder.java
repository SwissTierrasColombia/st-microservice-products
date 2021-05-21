package com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryStatuses;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryStatusResponse;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryStatusRepository;
import com.ai.st.microservice.quality.modules.shared.application.ListResponse;
import com.ai.st.microservice.quality.modules.shared.application.QueryUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.stream.Collectors;

@Service
public final class DeliveryStatusesFinder implements QueryUseCase<DeliveryStatusesFinderQuery, ListResponse<DeliveryStatusResponse>> {

    private final DeliveryStatusRepository repository;

    public DeliveryStatusesFinder(DeliveryStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public ListResponse<DeliveryStatusResponse> handle(DeliveryStatusesFinderQuery query) {
        return new ListResponse<>(repository.all().stream().map(DeliveryStatusResponse::fromAggregate).collect(Collectors.toList()));
    }

}
