package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.accept_delivery.DeliveryAcceptor;
import com.ai.st.microservice.quality.modules.deliveries.application.accept_delivery.DeliveryAcceptorCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.correct_delivery.DeliveryCorrection;
import com.ai.st.microservice.quality.modules.deliveries.application.correct_delivery.DeliveryCorrectionCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.reject_delivery.DeliveryRejecting;
import com.ai.st.microservice.quality.modules.deliveries.application.reject_delivery.DeliveryRejectingCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_operator.DeliveryToOperatorSender;
import com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_operator.DeliveryToOperatorSenderCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_manager.DeliveryToManagerSender;
import com.ai.st.microservice.quality.modules.deliveries.application.send_delivery_to_manager.DeliveryToManagerSenderCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.start_review.ReviewStarter;
import com.ai.st.microservice.quality.modules.deliveries.application.start_review.ReviewStarterCommand;
import com.ai.st.microservice.quality.modules.shared.application.Roles;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.TracingKeyword;
import io.swagger.annotations.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryPatchController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPatchController.class);

    private final DeliveryToManagerSender deliveryToManagerSender;
    private final ReviewStarter reviewStarter;
    private final DeliveryCorrection deliveryCorrection;
    private final DeliveryToOperatorSender deliveryToOperatorSender;
    private final DeliveryAcceptor deliveryAcceptor;
    private final DeliveryRejecting deliveryRejecting;

    public DeliveryPatchController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            OperatorBusiness operatorBusiness, DeliveryToManagerSender statusToDeliveredChanger,
            ReviewStarter reviewStarter, DeliveryCorrection deliveryCorrection,
            DeliveryToOperatorSender deliveryToOperatorSender, DeliveryAcceptor deliveryAcceptor,
            DeliveryRejecting deliveryRejecting) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryToManagerSender = statusToDeliveredChanger;
        this.reviewStarter = reviewStarter;
        this.deliveryCorrection = deliveryCorrection;
        this.deliveryToOperatorSender = deliveryToOperatorSender;
        this.deliveryAcceptor = deliveryAcceptor;
        this.deliveryRejecting = deliveryRejecting;
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/status/delivered", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to delivered")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to delivered"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToDelivered(@PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToDelivered");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);

            deliveryToManagerSender.handle(new DeliveryToManagerSenderCommand(deliveryId, session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToDelivered#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToDelivered#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToDelivered#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/status/review", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to review")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to review"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToReview(@PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToReview");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);

            if (session.role().equals(Roles.MANAGER)) {
                reviewStarter.handle(new ReviewStarterCommand(deliveryId, session.entityCode()));
            } else {
                deliveryCorrection.handle(new DeliveryCorrectionCommand(deliveryId, session.entityCode()));
            }

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToReview#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToReview#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToReview#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/status/remediation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to remediation")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to remediation"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToRemediation(@PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToRemediation");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);

            deliveryToOperatorSender.handle(new DeliveryToOperatorSenderCommand(deliveryId, session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToRemediation#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToRemediation#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToRemediation#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/status/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to accepted")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to accepted"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToAccepted(@PathVariable Long deliveryId,
            @RequestBody UpdateDeliveryStatusRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToAccepted");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateJustification(request.getJustification());

            deliveryAcceptor
                    .handle(new DeliveryAcceptorCommand(deliveryId, session.entityCode(), request.getJustification()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToAccepted#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToAccepted#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToAccepted#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/status/rejected", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change status to rejected")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Status changed to rejected"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> changeStatusToRejected(@PathVariable Long deliveryId,
            @RequestBody UpdateDeliveryStatusRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("changeStatusToRejected");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateJustification(request.getJustification());

            deliveryRejecting
                    .handle(new DeliveryRejectingCommand(deliveryId, session.entityCode(), request.getJustification()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryPatchController@changeStatusToRejected#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveryPatchController@changeStatusToRejected#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveryPatchController@changeStatusToRejected#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0)
            throw new InputValidationException("La entrega no es válida");
    }

    private void validateJustification(String justification) throws InputValidationException {
        if (justification == null || justification.isEmpty()) {
            throw new InputValidationException("La justificación es requerida.");
        }
        if (justification.length() > 500) {
            throw new InputValidationException("La justificación máximo debe tener 500 caracteres.");
        }
    }

}

@ApiModel(value = "UpdateDeliveryStatusRequest")
final class UpdateDeliveryStatusRequest {

    @ApiModelProperty(required = true, notes = "Justification")
    private String justification;

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    @Override
    public String toString() {
        return "UpdateDeliveryStatusRequest{" + "justification='" + justification + '\'' + '}';
    }
}
