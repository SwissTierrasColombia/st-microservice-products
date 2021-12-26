package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class UnauthorizedToModifyDelivery extends DomainError {

    public UnauthorizedToModifyDelivery(String message) {
        super("unauthorized_to_modify_delivery", message);
    }
}
