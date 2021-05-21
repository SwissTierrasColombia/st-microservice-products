package com.ai.st.microservice.quality;

import com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryProductStatus.DeliveryProductStatusCreator;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryProductStatus.DeliveryProductStatusCreatorCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryStatus.DeliveryStatusCreator;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDeliveryStatus.DeliveryStatusCreatorCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryProductStatuses.DeliveryProductStatusesFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryProductStatuses.DeliveryProductStatusesFinderQuery;
import com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryStatuses.DeliveryStatusesFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.FindAllDeliveryStatuses.DeliveryStatusesFinderQuery;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusId;
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

        int count = deliveryStatusesFinder.handle(new DeliveryStatusesFinderQuery()).size();
        if (count == 0) {
            deliveryStatusCreator.handle(new DeliveryStatusCreatorCommand(DeliveryStatusId.DRAFT, "BORRADOR"));
            deliveryStatusCreator.handle(new DeliveryStatusCreatorCommand(DeliveryStatusId.DELIVERED, "ENTREGADO"));
            deliveryStatusCreator.handle(new DeliveryStatusCreatorCommand(DeliveryStatusId.IN_REVIEW, "EN REVISIÓN"));
            deliveryStatusCreator.handle(new DeliveryStatusCreatorCommand(DeliveryStatusId.IN_REMEDIATION, "EN CORRECCIÓN"));
            deliveryStatusCreator.handle(new DeliveryStatusCreatorCommand(DeliveryStatusId.ACCEPTED, "ACEPTADO"));
            deliveryStatusCreator.handle(new DeliveryStatusCreatorCommand(DeliveryStatusId.REJECTED, "RECHAZADO"));
            log.info("The domains 'delivery statuses' have been loaded!");
        }

    }

    public void initProductStatuses() {

        int count = deliveryProductStatusesFinder.handle(new DeliveryProductStatusesFinderQuery()).size();
        if (count == 0) {
            deliveryProductStatusCreator.handle(new DeliveryProductStatusCreatorCommand(DeliveryProductStatusId.PENDING, "PENDIENTE"));
            deliveryProductStatusCreator.handle(new DeliveryProductStatusCreatorCommand(DeliveryProductStatusId.ACCEPTED, "ACEPTADO"));
            deliveryProductStatusCreator.handle(new DeliveryProductStatusCreatorCommand(DeliveryProductStatusId.REJECTED, "RECHAZADO"));
            log.info("The domains 'delivery product statuses' have been loaded!");
        }

    }

}
