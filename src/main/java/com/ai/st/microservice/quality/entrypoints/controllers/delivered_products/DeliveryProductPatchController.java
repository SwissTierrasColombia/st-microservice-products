package com.ai.st.microservice.quality.entrypoints.controllers.delivered_products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.delivered_products.application.evaluate_product.DeliveryProductEvaluator;
import com.ai.st.microservice.quality.modules.delivered_products.application.evaluate_product.DeliveryProductEvaluatorCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.TracingKeyword;
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

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryProductPatchController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductPatchController.class);

    private final DeliveryProductEvaluator deliveryProductEvaluator;

    public DeliveryProductPatchController(AdministrationBusiness administrationBusiness,
            ManagerBusiness managerBusiness, OperatorBusiness operatorBusiness,
            DeliveryProductEvaluator deliveryProductEvaluator) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryProductEvaluator = deliveryProductEvaluator;
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/status/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to accepted")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to accepted"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeProductStatusToAccepted(@PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId, @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeProductStatusToAccepted");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            deliveryProductEvaluator.handle(new DeliveryProductEvaluatorCommand(deliveryId, deliveryProductId,
                    session.entityCode(), DeliveryProductEvaluatorCommand.Statuses.ACCEPTED));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductPatchController@changeProductStatusToAccepted#Validation ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryProductPatchController@changeProductStatusToAccepted#Domain ---> "
                    + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryProductPatchController@changeProductStatusToAccepted#General ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/status/rejected", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to rejected")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to rejected"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeProductStatusToRejected(@PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId, @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeProductStatusToRejected");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            deliveryProductEvaluator.handle(new DeliveryProductEvaluatorCommand(deliveryId, deliveryProductId,
                    session.entityCode(), DeliveryProductEvaluatorCommand.Statuses.REJECTED));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductPatchController@changeProductStatusToRejected#Validation ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryProductPatchController@changeProductStatusToRejected#Domain ---> "
                    + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryProductPatchController@changeProductStatusToRejected#General ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId == null || deliveryId <= 0)
            throw new InputValidationException("La entrega no es válida.");
    }

    private void validateDeliveryProductId(Long deliveryProductId) throws InputValidationException {
        if (deliveryProductId == null || deliveryProductId <= 0)
            throw new InputValidationException("El producto de la entrega no es válido.");
    }

}
