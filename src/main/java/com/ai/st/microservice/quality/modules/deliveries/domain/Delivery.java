package com.ai.st.microservice.quality.modules.deliveries.domain;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductDate;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductObservations;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductStatusId;

import com.ai.st.microservice.quality.modules.products.domain.ProductId;

import com.ai.st.microservice.quality.modules.shared.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class Delivery extends AggregateRoot {

    private final DeliveryId id;
    private final DeliveryCode code;
    private final MunicipalityCode municipality;
    private final MunicipalityName municipalityName;
    private final DepartmentName departmentName;
    private final ManagerCode manager;
    private final ManagerName managerName;
    private final OperatorCode operator;
    private final OperatorName operatorName;
    private final UserCode user;
    private final DeliveryObservations observations;
    private final DeliveryDate deliveryDate;
    private final DeliveryStatusId deliveryStatusId;
    private final List<DeliveryProduct> deliveryProducts;

    public Delivery(DeliveryId id, DeliveryCode code, MunicipalityCode municipalityCode, MunicipalityName municipalityName, DepartmentName departmentName,
                    ManagerCode manager, ManagerName managerName, OperatorCode operatorCode, OperatorName operatorName, UserCode user,
                    DeliveryObservations observations, DeliveryDate deliveryDate, DeliveryStatusId deliveryStatusId, List<DeliveryProduct> deliveryProducts) {
        this.id = id;
        this.code = code;
        this.municipalityName = municipalityName;
        this.departmentName = departmentName;
        this.manager = manager;
        this.managerName = managerName;
        this.operator = operatorCode;
        this.municipality = municipalityCode;
        this.operatorName = operatorName;
        this.observations = observations;
        this.user = user;
        this.deliveryDate = deliveryDate;
        this.deliveryStatusId = deliveryStatusId;
        this.deliveryProducts = deliveryProducts;
    }

    public static Delivery create(DeliveryId id, DeliveryCode code, MunicipalityCode municipalityCode, MunicipalityName municipalityName,
                                  DepartmentName departmentName, ManagerCode manager, ManagerName managerName, OperatorCode operatorCode,
                                  OperatorName operatorName, UserCode user, DeliveryObservations observations, DeliveryDate date, DeliveryStatusId deliveryStatus) {
        return new Delivery(id, code, municipalityCode, municipalityName, departmentName, manager, managerName, operatorCode, operatorName, user, observations, date, deliveryStatus, new ArrayList<>());
    }

    public static Delivery create(DeliveryCode code, MunicipalityCode municipalityCode, MunicipalityName municipalityName, DepartmentName departmentName,
                                  ManagerCode manager, ManagerName managerName, OperatorCode operatorCode, OperatorName operatorName, UserCode user,
                                  DeliveryObservations observations, DeliveryDate date, DeliveryStatusId deliveryStatus) {
        return new Delivery(null, code, municipalityCode, municipalityName, departmentName, manager, managerName, operatorCode, operatorName, user, observations, date, deliveryStatus, new ArrayList<>());
    }

    public static Delivery fromPrimitives(Long id, String code, String municipalityCode, String municipalityName, String departmentName,
                                          Long managerCode, String managerName, Long operatorCode, String operatorName,
                                          Long userCode, String observations, Date createdAt, Long statusId) {
        return new Delivery(
                new DeliveryId(id),
                new DeliveryCode(code),
                new MunicipalityCode(municipalityCode),
                MunicipalityName.fromValue(municipalityName),
                DepartmentName.fromValue(departmentName),
                new ManagerCode(managerCode),
                ManagerName.fromValue(managerName),
                new OperatorCode(operatorCode),
                OperatorName.fromValue(operatorName),
                new UserCode(userCode),
                new DeliveryObservations(observations),
                new DeliveryDate(createdAt),
                new DeliveryStatusId(statusId),
                new ArrayList<>());
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

    public DeliveryCode code() {
        return code;
    }

    public MunicipalityName municipalityName() {
        return municipalityName;
    }

    public DepartmentName departmentName() {
        return departmentName;
    }

    public ManagerName managerName() {
        return managerName;
    }

    public OperatorName operatorName() {
        return operatorName;
    }

    public boolean isDraft() {
        return deliveryStatusId.value().equals(DeliveryStatusId.DRAFT);
    }

    public boolean isInReview() {
        return deliveryStatusId.value().equals(DeliveryStatusId.IN_REVIEW);
    }

    public boolean isDelivered() {
        return deliveryStatusId.value().equals(DeliveryStatusId.DELIVERED);
    }

    public boolean isInRemediation() {
        return deliveryStatusId.value().equals(DeliveryStatusId.IN_REMEDIATION);
    }

    public static List<DeliveryStatusId> statusesAllowedToManager() {
        return Arrays.asList(
                new DeliveryStatusId(DeliveryStatusId.DELIVERED),
                new DeliveryStatusId(DeliveryStatusId.IN_REVIEW),
                new DeliveryStatusId(DeliveryStatusId.IN_REMEDIATION),
                new DeliveryStatusId(DeliveryStatusId.ACCEPTED),
                new DeliveryStatusId(DeliveryStatusId.REJECTED)
        );
    }

    public static List<DeliveryStatusId> statusesAllowedToOperator() {
        return Arrays.asList(
                new DeliveryStatusId(DeliveryStatusId.DRAFT),
                new DeliveryStatusId(DeliveryStatusId.DELIVERED),
                new DeliveryStatusId(DeliveryStatusId.IN_REVIEW),
                new DeliveryStatusId(DeliveryStatusId.IN_REMEDIATION),
                new DeliveryStatusId(DeliveryStatusId.ACCEPTED),
                new DeliveryStatusId(DeliveryStatusId.REJECTED)
        );
    }

    public boolean isAvailableToManager() {
        DeliveryStatusId statusFound =
                statusesAllowedToManager().stream().filter(statusId -> statusId.value().equals(deliveryStatusId.value()))
                        .findAny().orElse(null);
        return statusFound != null;
    }

    public boolean deliveryBelongToManager(ManagerCode managerCode) {
        return managerCode.value().equals(manager.value());
    }

    public boolean deliveryBelongToOperator(OperatorCode operatorCode) {
        return operatorCode.value().equals(operator.value());
    }

}
