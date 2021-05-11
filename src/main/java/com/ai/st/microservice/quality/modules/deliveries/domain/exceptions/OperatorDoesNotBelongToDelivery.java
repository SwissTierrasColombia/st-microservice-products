package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class OperatorDoesNotBelongToDelivery extends DomainError {

    public OperatorDoesNotBelongToDelivery() {
        super("operator_does_not_belong_to_delivery", "El operador no pertenece a la entrega.");
    }
}
