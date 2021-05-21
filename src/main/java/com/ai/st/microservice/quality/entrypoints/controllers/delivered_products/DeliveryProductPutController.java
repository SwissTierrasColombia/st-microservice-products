package com.ai.st.microservice.quality.entrypoints.controllers.delivered_products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.delivered_products.application.update_product_from_delivery.DeliveryProductUpdater;
import com.ai.st.microservice.quality.modules.delivered_products.application.update_product_from_delivery.DeliveryProductUpdaterCommand;
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
public final class DeliveryProductPutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductPutController.class);

    private final DeliveryProductUpdater deliveryProductUpdater;

    public DeliveryProductPutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                        OperatorBusiness operatorBusiness, DeliveryProductUpdater deliveryProductUpdater) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryProductUpdater = deliveryProductUpdater;
    }

    @PutMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update product from delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> updateProductFromDelivery(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @RequestBody UpdateProductFromDeliveryRequest request,
            @RequestHeader("authorization") String headerAuthorization) {


        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            String observations = request.getObservations();
            validateObservations(observations);

            deliveryProductUpdater.handle(
                    new DeliveryProductUpdaterCommand(
                            deliveryId, deliveryProductId, observations, session.entityCode()
                    ));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductPutController@updateProductFromDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductPutController@updateProductFromDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductPutController@updateProductFromDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0)
            throw new InputValidationException("La entrega no es válida");
    }

    private void validateDeliveryProductId(Long deliveryProductId) throws InputValidationException {
        if (deliveryProductId <= 0)
            throw new InputValidationException("El producto no es válido");
    }

    private void validateObservations(String observations) throws InputValidationException {
        if (observations == null || observations.isEmpty())
            throw new InputValidationException("Las observaciones son requeridas.");
    }

}

@ApiModel(value = "UpdateProductFromDeliveryRequest")
final class UpdateProductFromDeliveryRequest {

    @ApiModelProperty(notes = "Observations")
    private String observations;

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}