package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery.CreateDeliveryCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery.DeliveryCreator;
import com.ai.st.microservice.quality.modules.shared.domain.*;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPostController.class);


    private final DeliveryCreator deliveryCreator;

    public DeliveryPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                  OperatorBusiness operatorBusiness, DeliveryCreator deliveryCreator) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryCreator = deliveryCreator;
    }

    @RequestMapping(value = "api/quality/v1/deliveries", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Delivery created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> createDelivery(@RequestBody CreateDeliveryRequest request,
                                            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            String municipalityCode = request.getMunicipalityCode();
            validateMunicipality(municipalityCode);

            String observations = request.getObservations();
            validateObservations(observations);

            Long managerCode = request.getManagerCode();
            validateManager(managerCode);

            List<Long> products = request.getDeliveredProducts();
            validateProducts(request.getDeliveredProducts());

            deliveryCreator.handle(
                    new CreateDeliveryCommand(
                            municipalityCode,
                            managerCode,
                            session.entityCode(),
                            session.userCode(),
                            observations,
                            products));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPostController@createDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryPostController@createDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryPostController@createDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }


        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
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
}