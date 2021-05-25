package com.ai.st.microservice.quality.entrypoints.controllers.feedbacks;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.feedbacks.application.FeedbackResponse;
import com.ai.st.microservice.quality.modules.feedbacks.application.find_feedbacks.FeedbackFinder;
import com.ai.st.microservice.quality.modules.feedbacks.application.find_feedbacks.FeedbackFinderQuery;
import com.ai.st.microservice.quality.modules.feedbacks.application.get_feedback_url.FeedbackURLGetter;
import com.ai.st.microservice.quality.modules.feedbacks.application.get_feedback_url.FeedbackURLGetterQuery;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import com.google.common.io.Files;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryProductFeedbackGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductFeedbackGetController.class);

    private final ServletContext servletContext;
    private final FeedbackFinder feedbackFinder;
    private final FeedbackURLGetter feedbackURLGetter;

    public DeliveryProductFeedbackGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                OperatorBusiness operatorBusiness, ServletContext servletContext, FeedbackFinder feedbackFinder, FeedbackURLGetter feedbackURLGetter) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.servletContext = servletContext;
        this.feedbackFinder = feedbackFinder;
        this.feedbackURLGetter = feedbackURLGetter;
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/feedbacks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get feedbacks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Feedback got", response = FeedbackResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> findFeedbacks(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @RequestHeader("authorization") String headerAuthorization) {


        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);

            responseDto = feedbackFinder.handle(
                    new FeedbackFinderQuery(
                            deliveryId, deliveryProductId, session.role(), session.entityCode()
                    )).list();

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductFeedbackGetController@findFeedbacks#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error DeliveryProductFeedbackGetController@findFeedbacks#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductFeedbackGetController@findFeedbacks#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/feedbacks/{feedbackId}/download")
    @ApiOperation(value = "Download file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "File downloaded"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> downloadFeedback(@PathVariable Long deliveryId,
                                              @PathVariable Long deliveryProductId,
                                              @PathVariable Long feedbackId,
                                              @RequestHeader("authorization") String headerAuthorization) {

        MediaType mediaType;
        File file;
        InputStreamResource resource;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateDeliveryProductId(deliveryProductId);
            validateFeedbackId(feedbackId);

            String pathFile = feedbackURLGetter.handle(
                    new FeedbackURLGetterQuery(
                            deliveryId, deliveryProductId, feedbackId, session.role(), session.entityCode()
                    )).value();

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

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductFeedbackGetController@downloadFeedback#Validation ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.getMessage(), 3), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (DomainError e) {
            log.error("Error DeliveryProductFeedbackGetController@downloadFeedback#Domain ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.errorMessage(), 2), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Error DeliveryProductFeedbackGetController@downloadFeedback#General ---> " + e.getMessage());
            return new ResponseEntity<>(new BasicResponseDto(e.getMessage(), 1), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType).contentLength(file.length())
                .header("extension", Files.getFileExtension(file.getName()))
                .header("filename", file.getName() + Files.getFileExtension(file.getName())).body(resource);
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

    private void validateFeedbackId(Long feedbackId) throws InputValidationException {
        if (feedbackId <= 0) {
            throw new InputValidationException("La retroalimentación no es válida");
        }
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}
