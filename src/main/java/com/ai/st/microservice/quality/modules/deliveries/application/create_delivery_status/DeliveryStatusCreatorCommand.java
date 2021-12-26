package com.ai.st.microservice.quality.modules.deliveries.application.create_delivery_status;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class DeliveryStatusCreatorCommand implements Command {

    private final Long id;
    private final String name;

    public DeliveryStatusCreatorCommand(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
    
}
