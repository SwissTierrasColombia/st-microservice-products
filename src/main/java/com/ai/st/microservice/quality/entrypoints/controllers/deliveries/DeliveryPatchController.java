package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.SendDeliveryToManager.DeliveryToManagerSender;
import com.ai.st.microservice.quality.modules.deliveries.application.SendDeliveryToManager.DeliveryToManagerSenderCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryPatchController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPatchController.class);

    private final DeliveryToManagerSender deliveryToManagerSender;

    public DeliveryPatchController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                   OperatorBusiness operatorBusiness, DeliveryToManagerSender statusToDeliveredChanger) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryToManagerSender = statusToDeliveredChanger;
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/status/delivered", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Send delivery to manager")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delivery sent"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> sendDeliveryToManager(
            @PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);

            deliveryToManagerSender.handle(
                    new DeliveryToManagerSenderCommand(
                            deliveryId, session.entityCode()
                    ));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@sendDeliveryToManager#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@sendDeliveryToManager#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@sendDeliveryToManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0)
            throw new InputValidationException("La entrega no es válida");
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

}
