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
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
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

import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryProductFeedbackGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductFeedbackGetController.class);

    private final FeedbackFinder feedbackFinder;

    public DeliveryProductFeedbackGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                OperatorBusiness operatorBusiness, FeedbackFinder feedbackFinder) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.feedbackFinder = feedbackFinder;
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

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}
