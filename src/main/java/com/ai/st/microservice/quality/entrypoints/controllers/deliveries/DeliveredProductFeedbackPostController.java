package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery.CreateDeliveryCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Objects;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveredProductFeedbackPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveredProductFeedbackPostController.class);

    public DeliveredProductFeedbackPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                  OperatorBusiness operatorBusiness) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
    }


    @PostMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create feedback")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Feedback created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> createFeedback(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @ModelAttribute CreateFeedbackRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);
            validateAttachment(request.getAttachment());
            validateFeedback(request.getFeedback(), request.getStatus());



            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error DeliveredProductFeedbackPostController@createFeedback#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveredProductFeedbackPostController@createFeedback#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveredProductFeedbackPostController@createFeedback#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
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
            String extension = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
            boolean isZip = extension.equalsIgnoreCase("zip");
            if (!isZip) {
                throw new InputValidationException("El adjunto debe cargarse en formato zip");
            }
        }
    }

    private void validateFeedback(String feedback, CreateFeedbackRequest.Statuses status) throws InputValidationException {
        if (status.equals(CreateFeedbackRequest.Statuses.REJECTED) && (feedback == null || feedback.isEmpty())) {
            throw new InputValidationException("Se debe enviar comentarios de retroalimentación al rechazar el producto.");
        }
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

}


@ApiModel(value = "CreateFeedbackRequest")
final class CreateFeedbackRequest {

    enum Statuses {ACCEPTED, REJECTED}

    @ApiModelProperty(notes = "Status", required = true)
    private Statuses status;

    @ApiModelProperty(notes = "Feedback")
    private String feedback;

    @ApiModelProperty(notes = "Attachment")
    private MultipartFile attachment;

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

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
}