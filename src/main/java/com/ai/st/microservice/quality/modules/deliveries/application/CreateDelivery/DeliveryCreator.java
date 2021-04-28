package com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryObservations;
import com.ai.st.microservice.quality.modules.shared.domain.*;


@Service
public final class DeliveryCreator {

    /*private final DeliveryRepository repository;

    public DeliveryCreator(DeliveryRepository repository) {
        this.repository = repository;
    }*/

    public void create(Long id, MunicipalityCode municipality, ManagerCode manager, OperatorCode operator,
                       UserCode user, DeliveryObservations observations) {

    }

}
