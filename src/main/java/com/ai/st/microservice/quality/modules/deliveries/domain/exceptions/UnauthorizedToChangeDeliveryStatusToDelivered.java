package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class UnauthorizedToChangeDeliveryStatusToDelivered extends DomainError {

    public UnauthorizedToChangeDeliveryStatusToDelivered(String errorMessage) {
        super("unauthorized_to_change_delivery_status_to_delivered", errorMessage);
    }
}
