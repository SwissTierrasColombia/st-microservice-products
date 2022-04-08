package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.create_delivery.CreateDeliveryCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.create_delivery.DeliveryCreator;
import com.ai.st.microservice.quality.modules.shared.domain.*;

import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.TracingKeyword;
import io.swagger.annotations.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPostController.class);

    private final DeliveryCreator deliveryCreator;

    public DeliveryPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            OperatorBusiness operatorBusiness, DeliveryCreator deliveryCreator) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryCreator = deliveryCreator;
    }

    @PostMapping(value = "api/quality/v1/deliveries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create delivery")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Delivery created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> createDelivery(@RequestBody CreateDeliveryRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("createDelivery");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            InformationSession session = this.getInformationSession(headerAuthorization);

            String municipalityCode = request.getMunicipalityCode();
            validateMunicipality(municipalityCode);

            String observations = request.getObservations();
            validateObservations(observations);

            Long managerCode = request.getManagerCode();
            validateManager(managerCode);

            List<Long> products = request.getDeliveredProducts();
            validateProducts(request.getDeliveredProducts());

            deliveryCreator.handle(new CreateDeliveryCommand(municipalityCode, managerCode, session.entityCode(),
                    session.userCode(), observations, products));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPostController@createDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPostController@createDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPostController@createDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateMunicipality(String municipalityCode) throws InputValidationException {
        if (municipalityCode == null || municipalityCode.isEmpty()) {
            throw new InputValidationException("El municipio es requerido.");
        }
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty()) {
            throw new InputValidationException("Las observaciones de la entrega son obligatorias.");
        }
    }

    private void validateManager(Long managerCode) throws InputValidationException {
        if (managerCode == null || managerCode <= 0) {
            throw new InputValidationException("El gestor es requerido.");
        }
    }

    private void validateProducts(List<Long> products) throws InputValidationException {
        for (Long productId : products) {
            if (productId == null || productId <= 0) {
                throw new InputValidationException("Los productos no son válidos.");
            }
        }
    }

}

@ApiModel(value = "CreateDeliveryRequest")
final class CreateDeliveryRequest {

    @ApiModelProperty(required = true, notes = "Municipality ID")
    private String municipalityCode;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    @ApiModelProperty(required = true, notes = "Manager Code")
    private Long managerCode;

    @ApiModelProperty(notes = "List of products ID")
    private List<Long> deliveredProducts;

    public CreateDeliveryRequest() {
        this.deliveredProducts = new ArrayList<>();
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(Long managerCode) {
        this.managerCode = managerCode;
    }

    public List<Long> getDeliveredProducts() {
        return deliveredProducts;
    }

    public void setDeliveredProducts(List<Long> deliveredProducts) {
        this.deliveredProducts = deliveredProducts;
    }

    @Override
    public String toString() {
        return "CreateDeliveryRequest{" + "municipalityCode='" + municipalityCode + '\'' + ", observations='"
                + observations + '\'' + ", managerCode=" + managerCode + ", deliveredProducts=" + deliveredProducts
                + '}';
    }
}