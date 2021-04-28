package com.ai.st.microservice.quality.modules.deliveries.application.SearchAllDeliveryProductStatuses;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryProductStatusResponse;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductStatusRepository;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class DeliveryProductStatusesFinder {

    private final DeliveryProductStatusRepository repository;

    public DeliveryProductStatusesFinder(DeliveryProductStatusRepository repository) {
        this.repository = repository;
    }

    public List<DeliveryProductStatusResponse> finder() {
        return repository.all().stream().map(DeliveryProductStatusResponse::fromAggregate).collect(Collectors.toList());
    }

}
