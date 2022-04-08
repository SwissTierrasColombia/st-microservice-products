package com.ai.st.microservice.quality.entrypoints.controllers.attachments;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.attachments.application.AttachmentProductResponse;
import com.ai.st.microservice.quality.modules.attachments.application.find_attachment_from_product.AttachmentFinder;
import com.ai.st.microservice.quality.modules.attachments.application.find_attachment_from_product.AttachmentFinderQuery;
import com.ai.st.microservice.quality.modules.attachments.application.find_attachments_from_product.AttachmentsProductFinder;
import com.ai.st.microservice.quality.modules.attachments.application.find_attachments_from_product.AttachmentsProductFinderQuery;
import com.ai.st.microservice.quality.modules.attachments.application.get_attachment_report_url.AttachmentReportURLGetter;
import com.ai.st.microservice.quality.modules.attachments.application.get_attachment_report_url.AttachmentReportURLGetterQuery;
import com.ai.st.microservice.quality.modules.attachments.application.get_attachment_url.AttachmentURLGetter;
import com.ai.st.microservice.quality.modules.attachments.application.get_attachment_url.AttachmentURLGetterQuery;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryProductAttachmentGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductAttachmentGetController.class);

    private final ServletContext servletContext;
    private final AttachmentFinder attachmentFinder;
    private final AttachmentsProductFinder attachmentsProductFinder;
    private final AttachmentURLGetter attachmentURLGetter;
    private final AttachmentReportURLGetter attachmentReportURLGetter;

    public DeliveryProductAttachmentGetController(AdministrationBusiness administrationBusiness,
            ManagerBusiness managerBusiness, OperatorBusiness operatorBusiness, ServletContext servletContext,
            AttachmentFinder attachmentFinder, AttachmentsProductFinder attachmentsProductFinder,
            AttachmentURLGetter attachmentURLGetter, AttachmentReportURLGetter attachmentReportURLGetter) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.servletContext = servletContext;
        this.attachmentFinder = attachmentFinder;
        this.attachmentsProductFinder = attachmentsProductFinder;
        this.attachmentURLGetter = attachmentURLGetter;
        this.attachmentReportURLGetter = attachmentReportURLGetter;
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get attachments from delivery product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Attachments got", response = AttachmentProductResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> findAttachmentsFromProduct(@PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId, @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            responseDto = attachmentsProductFinder.handle(new AttachmentsProductFinderQuery(deliveryId,
                    deliveryProductId, session.role(), session.entityCode())).list();

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentGetController@findAttachmentsFromProduct#Domain ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentGetController@findAttachmentsFromProduct#General ---> "
                    + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/xtf-attachments/{attachmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get attachment from delivery product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Attachment got", response = AttachmentProductResponse.class),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> findXTFAttachment(@PathVariable Long deliveryId, @PathVariable Long deliveryProductId,
            @PathVariable Long attachmentId) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            responseDto = attachmentFinder
                    .handle(new AttachmentFinderQuery(deliveryId, deliveryProductId, attachmentId, true));

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentGetController@findXTFAttachment#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentGetController@findXTFAttachment#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);

    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}/download")
    @ApiOperation(value = "Download file")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "File downloaded"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> downloadAttachment(@PathVariable Long deliveryId, @PathVariable Long deliveryProductId,
            @PathVariable Long attachmentId, @RequestHeader("authorization") String headerAuthorization) {

        MediaType mediaType;
        File file;
        InputStreamResource resource;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            String pathFile = attachmentURLGetter.handle(new AttachmentURLGetterQuery(deliveryId, deliveryProductId,
                    attachmentId, session.role(), session.entityCode())).value();

            Path path = Paths.get(pathFile);
            String fileName = path.getFileName().toString();

            String mineType = servletContext.getMimeType(fileName);

            try {
                mediaType = MediaType.parseMediaType(mineType);
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            file = new File(pathFile);
            resource = new InputStreamResource(new FileInputStream(file));

        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentGetController@downloadAttachment#Domain ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.errorMessage(), 2), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentGetController@downloadAttachment#General ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.getMessage(), 1), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseFile(file, mediaType, resource);
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments/{attachmentId}/report-download")
    @ApiOperation(value = "Download report file")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "File downloaded"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class) })
    @ResponseBody
    public ResponseEntity<?> downloadReportAttachment(@PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId, @PathVariable Long attachmentId,
            @RequestHeader("authorization") String headerAuthorization) {

        MediaType mediaType;
        File file;
        InputStreamResource resource;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);
            validateAttachmentId(attachmentId);

            String pathFile = attachmentReportURLGetter.handle(new AttachmentReportURLGetterQuery(deliveryId,
                    deliveryProductId, attachmentId, session.entityCode())).value();

            Path path = Paths.get(pathFile);
            String fileName = path.getFileName().toString();

            String mineType = servletContext.getMimeType(fileName);

            try {
                mediaType = MediaType.parseMediaType(mineType);
            } catch (Exception e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            file = new File(pathFile);
            resource = new InputStreamResource(new FileInputStream(file));

        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentGetController@downloadReportAttachment#Domain ---> "
                    + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.errorMessage(), 2), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentGetController@downloadReportAttachment#General ---> "
                    + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.getMessage(), 1), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return this.responseFile(file, mediaType, resource);
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

}
