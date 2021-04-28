package com.ai.st.microservice.quality;

import com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryProductStatus.DeliveryProductStatusCreator;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryStatus.DeliveryStatusCreator;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchAllDeliveryProductStatuses.DeliveryProductStatusesFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.SearchAllDeliveryStatuses.DeliveryStatusesFinder;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryProductStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StMicroserviceProductsApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log = LoggerFactory.getLogger(StMicroserviceProductsApplicationStartup.class);

    private final DeliveryStatusCreator deliveryStatusCreator;
    private final DeliveryStatusesFinder deliveryStatusesFinder;
    private final DeliveryProductStatusCreator deliveryProductStatusCreator;
    private final DeliveryProductStatusesFinder deliveryProductStatusesFinder;

    public StMicroserviceProductsApplicationStartup(DeliveryStatusCreator deliveryStatusCreator,
                                                    DeliveryStatusesFinder deliveryStatusesFinder,
                                                    DeliveryProductStatusCreator deliveryProductStatusCreator,
                                                    DeliveryProductStatusesFinder deliveryProductStatusesFinder) {
        this.deliveryStatusCreator = deliveryStatusCreator;
        this.deliveryStatusesFinder = deliveryStatusesFinder;
        this.deliveryProductStatusCreator = deliveryProductStatusCreator;
        this.deliveryProductStatusesFinder = deliveryProductStatusesFinder;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("ST - Loading Domains ... ");

        this.initDeliveryStatuses();
        this.initProductStatuses();
    }

    public void initDeliveryStatuses() {

        int count = deliveryStatusesFinder.finder().size();
        if (count == 0) {
            deliveryStatusCreator.create(DeliveryStatusId.IN_VALIDATION, "EN VALIDACIÓN");
            deliveryStatusCreator.create(DeliveryStatusId.DELIVERED, "ENTREGADO");
            deliveryStatusCreator.create(DeliveryStatusId.REJECT, "RECHAZADO");
            log.info("The domains 'delivery statuses' have been loaded!");
        }

    }

    public void initProductStatuses() {

        int count = deliveryProductStatusesFinder.finder().size();
        if (count == 0) {
            deliveryProductStatusCreator.create(DeliveryProductStatusId.IN_VALIDATION, "EN VALIDACIÓN");
            deliveryProductStatusCreator.create(DeliveryProductStatusId.ACCEPT, "ACEPTADO");
            deliveryProductStatusCreator.create(DeliveryProductStatusId.REJECT, "RECHAZADO");
            log.info("The domains 'delivery product statuses' have been loaded!");
        }

    }


}
