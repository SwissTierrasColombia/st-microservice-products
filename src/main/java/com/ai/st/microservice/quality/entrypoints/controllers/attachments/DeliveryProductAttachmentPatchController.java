package com.ai.st.microservice.quality.entrypoints.controllers.attachments;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.attachments.application.update_xtf_status.XTFStatusUpdater;
import com.ai.st.microservice.quality.modules.attachments.application.update_xtf_status.XTFStatusUpdaterCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Attachments", tags = {"Attachments"})
@RestController
public final class DeliveryProductAttachmentPatchController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductAttachmentPostController.class);

    private final XTFStatusUpdater xtfStatusUpdater;

    public DeliveryProductAttachmentPatchController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                    OperatorBusiness operatorBusiness, XTFStatusUpdater xtfStatusUpdater) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.xtfStatusUpdater = xtfStatusUpdater;
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}/status/quality-process-finished", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update XTF Status to Quality Process Finished")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Status XTF updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> updateXTFStatusToQualityProcessFinished(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @PathVariable Long attachmentId) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            validateDeliveryId(deliveryId);
            validateProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            xtfStatusUpdater.handle(new XTFStatusUpdaterCommand(
                    XTFStatusUpdaterCommand.Status.QUALITY_PROCESS_FINISHED, attachmentId));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductAttachmentPatchController@updateXTFStatusToQualityProcessFinished#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentPatchController@updateXTFStatusToQualityProcessFinished#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentPatchController@updateXTFStatusToQualityProcessFinished#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 4);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @PatchMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}/status/accepted", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update XTF Status to Quality Process Finished")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Status XTF updated"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> updateXTFStatusToAccepted(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @PathVariable Long attachmentId) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            validateDeliveryId(deliveryId);
            validateProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            xtfStatusUpdater.handle(new XTFStatusUpdaterCommand(
                    XTFStatusUpdaterCommand.Status.ACCEPTED, attachmentId));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductAttachmentPatchController@updateXTFStatusToQualityProcessFinished#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentPatchController@updateXTFStatusToQualityProcessFinished#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentPatchController@updateXTFStatusToQualityProcessFinished#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 4);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida");
        }
    }

    private void validateProductId(Long productId) throws InputValidationException {
        if (productId <= 0) {
            throw new InputValidationException("El producto no es válido");
        }
    }

    private void validateAttachmentId(Long attachmentId) throws InputValidationException {
        if (attachmentId <= 0) {
            throw new InputValidationException("El adjunto no es válido");
        }
    }

}