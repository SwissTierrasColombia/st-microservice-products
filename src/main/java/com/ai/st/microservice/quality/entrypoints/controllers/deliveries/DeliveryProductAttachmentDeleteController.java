package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.RemoveAttachmentFromProduct.AttachmentProductRemover;
import com.ai.st.microservice.quality.modules.deliveries.application.RemoveAttachmentFromProduct.AttachmentProductRemoverCommand;
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
public final class DeliveryProductAttachmentDeleteController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductAttachmentDeleteController.class);

    private final AttachmentProductRemover attachmentProductRemover;

    public DeliveryProductAttachmentDeleteController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                     OperatorBusiness operatorBusiness, AttachmentProductRemover attachmentProductRemover) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.attachmentProductRemover = attachmentProductRemover;
    }

    @DeleteMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}")
    @ApiOperation(value = "Remove attachment from delivery product")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Attachment removed"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> removeAttachmentFromProduct(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @PathVariable Long attachmentId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            attachmentProductRemover.remove(
                    new AttachmentProductRemoverCommand(
                            deliveryId, deliveryProductId, attachmentId, session.entityCode()));

            httpStatus = HttpStatus.NO_CONTENT;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductAttachmentDeleteController@removeAttachmentFromProduct#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentDeleteController@removeAttachmentFromProduct#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentDeleteController@removeAttachmentFromProduct#General ---> " + e.getMessage());
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

    private void validateAttachmentId(Long attachmentId) throws InputValidationException {
        if (attachmentId == null || attachmentId <= 0) {
            throw new InputValidationException("El adjunto no válido.");
        }
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

}
