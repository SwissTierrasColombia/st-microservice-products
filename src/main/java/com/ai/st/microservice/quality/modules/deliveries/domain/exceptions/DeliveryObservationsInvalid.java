package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class DeliveryObservationsInvalid extends DomainError {

    public DeliveryObservationsInvalid() {
        super("observations_invalid", "Las observaciones son obligatorias");
    }

}
