package com.ai.st.microservice.quality.entrypoints.controllers.attachments;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.attachments.application.add_report_revision.ReportAggregator;
import com.ai.st.microservice.quality.modules.attachments.application.add_report_revision.ReportAggregatorCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.CompressorFile;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Api(value = "Manage Attachments", tags = {"Attachments"})
@RestController
public final class DeliveryProductAttachmentPutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductAttachmentPostController.class);

    private final ReportAggregator reportAggregator;
    private final StoreFile storeFile;
    private final CompressorFile compressorFile;

    public DeliveryProductAttachmentPutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                  OperatorBusiness operatorBusiness,
                                                  ReportAggregator reportAggregator, StoreFile storeFile,
                                                  CompressorFile compressorFile) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.reportAggregator = reportAggregator;
        this.storeFile = storeFile;
        this.compressorFile = compressorFile;
    }

    @PutMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}/report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add report to xtf attachment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Report added"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> addReportToXTFAttachment(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @PathVariable Long attachmentId,
            @ModelAttribute RevisionXTFAttachmentRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            validateReportXTFAttachment(request);

            reportAggregator.handle(new ReportAggregatorCommand(
                    deliveryId, deliveryProductId, attachmentId, session.entityCode(),
                    session.userCode(), request.isOverwriteReport(), request.getAttachment().getBytes(), "zip"));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductAttachmentPutController@addReportToXTFAttachment#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentPutController@addReportToXTFAttachment#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentPutController@addReportToXTFAttachment#General ---> " + e.getMessage());
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

    private void validateReportXTFAttachment(RevisionXTFAttachmentRequest request)
            throws InputValidationException, IOException {
        MultipartFile file = request.getAttachment();
        if (file == null) {
            throw new InputValidationException("Se debe cargar un archivo adjunto.");
        }
        String extension = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
        boolean isZip = extension.equalsIgnoreCase("zip");
        if (!isZip) {
            throw new InputValidationException("El archivo debe cargarse en formato zip");
        }

        String temporalFilePath = storeFile.storeFileTemporarily(file.getBytes(), extension);
        int countEntries = compressorFile.countEntries(temporalFilePath);
        if (countEntries < 1 || countEntries > 2) {
            throw new InputValidationException("El comprimido debe contener mínimo uno y máximo dos archivos.");
        }

        boolean filePresent = compressorFile.checkIfFileIsPresent(temporalFilePath, "pdf");
        if (!filePresent) {
            throw new InputValidationException("El comprimido no contiene un archivo en formato XTF correspondiente al reporte de revisión.");
        }

        if (countEntries == 2) {
            boolean fileGPKGPresent = compressorFile.checkIfFileIsPresent(temporalFilePath, "gpkg");
            if (!fileGPKGPresent) {
                throw new InputValidationException("El comprimido sólo acepta de forma opcional archivos gpkg (GeoPackage).");
            }
        }

        storeFile.deleteFile(temporalFilePath);
    }

}

@ApiModel(value = "RevisionXTFAttachmentRequest")
final class RevisionXTFAttachmentRequest {

    @ApiModelProperty(required = true, notes = "Attachment")
    private MultipartFile attachment;

    @ApiModelProperty(notes = "Overwrite report?")
    private boolean overwriteReport;

    public RevisionXTFAttachmentRequest() {
        this.overwriteReport = false;
    }

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }

    public boolean isOverwriteReport() {
        return overwriteReport;
    }

    public void setOverwriteReport(boolean overwriteReport) {
        this.overwriteReport = overwriteReport;
    }
}

