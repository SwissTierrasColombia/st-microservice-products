package com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryStatuses;

import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryStatusResponse;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryStatusRepository;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class DeliveryStatusesFinder {

    private final DeliveryStatusRepository repository;

    public DeliveryStatusesFinder(DeliveryStatusRepository repository) {
        this.repository = repository;
    }

    public List<DeliveryStatusResponse> finder() {
        return repository.all().stream().map(DeliveryStatusResponse::fromAggregate).collect(Collectors.toList());
    }

}
