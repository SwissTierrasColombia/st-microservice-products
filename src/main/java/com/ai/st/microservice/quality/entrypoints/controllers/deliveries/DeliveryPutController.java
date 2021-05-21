package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.update_delivery.DeliveryUpdater;
import com.ai.st.microservice.quality.modules.deliveries.application.update_delivery.DeliveryUpdaterCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryPutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPutController.class);

    private final DeliveryUpdater deliveryUpdater;

    public DeliveryPutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                 OperatorBusiness operatorBusiness, DeliveryUpdater deliveryUpdater) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryUpdater = deliveryUpdater;
    }

    @PutMapping(value = "api/quality/v1/deliveries/{deliveryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update product from delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> updateDelivery(
            @PathVariable Long deliveryId,
            @RequestBody UpdateDeliveryRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);

            String observations = request.getObservations();
            validateObservations(observations);

            deliveryUpdater.handle(
                    new DeliveryUpdaterCommand(
                            deliveryId, observations, session.entityCode()
                    ));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPutController@updateDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryPutController@updateDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryPutController@updateDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0)
            throw new InputValidationException("La entrega no es válida");
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty())
            throw new InputValidationException("Las observaciones son requeridas.");
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}

@ApiModel(value = "UpdateDeliveryRequest")
final class UpdateDeliveryRequest {

    @ApiModelProperty(notes = "Observations")
    private String observations;

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
