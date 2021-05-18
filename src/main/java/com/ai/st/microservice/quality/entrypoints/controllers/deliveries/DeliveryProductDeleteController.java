package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.RemoveProductFromDelivery.DeliveryProductRemover;
import com.ai.st.microservice.quality.modules.deliveries.application.RemoveProductFromDelivery.DeliveryProductRemoverCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryProductDeleteController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductDeleteController.class);

    private final DeliveryProductRemover deliveryProductRemover;

    public DeliveryProductDeleteController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                           OperatorBusiness operatorBusiness, DeliveryProductRemover deliveryProductRemover) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryProductRemover = deliveryProductRemover;
    }

    @DeleteMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}")
    @ApiOperation(value = "Remove product from delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Product removed"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> removeProductFromDelivery(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            deliveryProductRemover.remove(
                    new DeliveryProductRemoverCommand(
                            deliveryId, deliveryProductId, session.entityCode()
                    ));

            httpStatus = HttpStatus.NO_CONTENT;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductDeleteController@removeProductFromDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error DeliveryProductDeleteController@removeProductFromDelivery#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductDeleteController@removeProductFromDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);


    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId == null || deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida.");
        }
    }

    private void validateDeliveryProductId(Long deliveryProductId) throws InputValidationException {
        if (deliveryProductId == null || deliveryProductId <= 0) {
            throw new InputValidationException("El producto de la entrega no es válido.");
        }
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

}
