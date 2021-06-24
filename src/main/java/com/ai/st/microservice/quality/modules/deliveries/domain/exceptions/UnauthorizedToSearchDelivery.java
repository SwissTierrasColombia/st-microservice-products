package com.ai.st.microservice.quality.modules.deliveries.domain.exceptions;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

public final class UnauthorizedToSearchDelivery extends DomainError {

    public UnauthorizedToSearchDelivery() {
        super("unauthorized_to_search_delivery", "Sin autorización para consultar la entrega.");
    }
}
