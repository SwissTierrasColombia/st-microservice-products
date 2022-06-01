package com.ai.st.microservice.quality.entrypoints.controllers.feedbacks;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.feedbacks.application.create_feedback.FeedbackCreator;
import com.ai.st.microservice.quality.modules.feedbacks.application.create_feedback.FeedbackCreatorCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.TracingKeyword;
import io.swagger.annotations.*;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryProductFeedbackPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductFeedbackPostController.class);

    private final FeedbackCreator feedbackCreator;

    public DeliveryProductFeedbackPostController(AdministrationBusiness administrationBusiness,
            ManagerBusiness managerBusiness, OperatorBusiness operatorBusiness, FeedbackCreator feedbackCreator) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.feedbackCreator = feedbackCreator;
    }

    @PostMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create feedback")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Feedback created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
    @ResponseBody
    public ResponseEntity<?> createFeedback(@PathVariable Long deliveryId, @PathVariable Long deliveryProductId,
            @ModelAttribute CreateFeedbackRequest request, @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("createFeedback");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);
            SCMTracing.addCustomParameter(TracingKeyword.BODY_REQUEST, request.toString());

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            MultipartFile attachment = request.getAttachment();
            validateAttachment(attachment);

            String feedback = request.getFeedback();
            validateFeedback(feedback);

            feedbackCreator.handle(new FeedbackCreatorCommand(deliveryId, deliveryProductId, session.entityCode(),
                    feedback, (attachment != null) ? attachment.getBytes() : null, getExtensionFile(attachment)));

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error DeliveredProductFeedbackPostController@createFeedback#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error DeliveredProductFeedbackPostController@createFeedback#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error DeliveredProductFeedbackPostController@createFeedback#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida");
        }
    }

    private void validateDeliveryProductId(Long deliveryProductId) throws InputValidationException {
        if (deliveryProductId <= 0) {
            throw new InputValidationException("El producto no es válido");
        }
    }

    private void validateAttachment(MultipartFile file) throws InputValidationException {
        if (file != null) {
            String extension = getExtensionFile(file);
            boolean isZip = extension.equalsIgnoreCase("zip");
            if (!isZip) {
                throw new InputValidationException("El adjunto debe cargarse en formato zip");
            }
        }
    }

    private void validateFeedback(String feedback) throws InputValidationException {
        if (feedback == null || feedback.isEmpty()) {
            throw new InputValidationException("Los comentarios de retroalimentación son requeridos.");
        }
    }

    private String getExtensionFile(MultipartFile file) {
        return (file == null) ? null : Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
    }

}

@ApiModel(value = "CreateFeedbackRequest")
final class CreateFeedbackRequest {

    @ApiModelProperty(notes = "Feedback", required = true)
    private String feedback;

    @ApiModelProperty(notes = "Attachment")
    private MultipartFile attachment;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "CreateFeedbackRequest{" + "feedback='" + feedback + '\'' + ", attachment=" + attachment.toString()
                + '}';
    }
}