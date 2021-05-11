package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductDate;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductObservations;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProductStatusId;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.shared.domain.*;

import java.util.ArrayList;
import java.util.List;

public final class Delivery extends AggregateRoot {

    private final DeliveryId id;
    private final ManagerCode manager;
    private final OperatorCode operator;
    private final MunicipalityCode municipality;
    private final DeliveryObservations observations;
    private final DeliveryDate deliveryDate;
    private final UserCode user;
    private final DeliveryStatusId deliveryStatusId;
    private final List<DeliveryProduct> deliveryProducts;

    public Delivery(DeliveryId id, MunicipalityCode municipalityCode, ManagerCode manager, OperatorCode operatorCode,
                    UserCode user, DeliveryObservations observations, DeliveryDate deliveryDate, DeliveryStatusId deliveryStatusId,
                    List<DeliveryProduct> deliveryProducts) {
        this.id = id;
        this.manager = manager;
        this.operator = operatorCode;
        this.municipality = municipalityCode;
        this.observations = observations;
        this.user = user;
        this.deliveryDate = deliveryDate;
        this.deliveryStatusId = deliveryStatusId;
        this.deliveryProducts = deliveryProducts;
    }

    public static Delivery create(MunicipalityCode municipalityCode, ManagerCode manager, OperatorCode operatorCode,
                                  UserCode user, DeliveryObservations observations, DeliveryDate date, DeliveryStatusId deliveryStatusId) {
        return new Delivery(null, municipalityCode, manager, operatorCode, user, observations, date, deliveryStatusId, new ArrayList<>());
    }

    public void addProduct(DeliveryProductDate deliveryProductDate, DeliveryProductObservations deliveryProductObservations,
                           ProductId productId, DeliveryProductStatusId deliveryProductStatusId) {
        this.deliveryProducts.add(new DeliveryProduct(
                null,
                deliveryProductDate,
                deliveryProductObservations,
                productId,
                deliveryProductStatusId,
                new ArrayList<>()));
    }

    public DeliveryId id() {
        return id;
    }

    public ManagerCode manager() {
        return manager;
    }

    public OperatorCode operator() {
        return operator;
    }

    public MunicipalityCode municipality() {
        return municipality;
    }

    public DeliveryObservations observations() {
        return observations;
    }

    public UserCode user() {
        return user;
    }

    public DeliveryDate deliveryDate() {
        return deliveryDate;
    }

    public List<DeliveryProduct> deliveryProducts() {
        return deliveryProducts;
    }

    public DeliveryStatusId deliveryStatusId() {
        return deliveryStatusId;
    }

}
