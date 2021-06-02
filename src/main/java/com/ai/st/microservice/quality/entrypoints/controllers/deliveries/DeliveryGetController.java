package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.DeliveryResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.find_deliveries.DeliveriesFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.find_deliveries.DeliveriesFinderQuery;
import com.ai.st.microservice.quality.modules.deliveries.application.search_delivery.DeliverySearcher;
import com.ai.st.microservice.quality.modules.deliveries.application.search_delivery.DeliverySearcherQuery;
import com.ai.st.microservice.quality.modules.shared.application.PageableResponse;
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

import java.util.List;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryGetController.class);

    private final DeliveriesFinder deliveriesFinder;
    private final DeliverySearcher deliverySearcher;

    public DeliveryGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                 OperatorBusiness operatorBusiness, DeliveriesFinder deliveriesFinder, DeliverySearcher deliverySearcher) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveriesFinder = deliveriesFinder;
        this.deliverySearcher = deliverySearcher;
    }

    @GetMapping(value = "api/quality/v1/deliveries", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get deliveries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deliveries got", response = PageableResponse.class),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> findDeliveries(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "states", required = false) List<Long> states,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "municipality", required = false) String municipality,
            @RequestParam(name = "operator", required = false) Long operator,
            @RequestParam(name = "manager", required = false) Long manager,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            responseDto = deliveriesFinder.handle(
                    new DeliveriesFinderQuery(
                            page,
                            limit,
                            states,
                            code,
                            municipality,
                            operator,
                            manager,
                            session.role(),
                            session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error DeliveryGetController@findDeliveries#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryGetController@findDeliveries#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delivery got", response = DeliveryResponse.class),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> searchDelivery(
            @PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            if (deliveryId <= 0) {
                throw new InputValidationException("El ID de la entrega no es válido.");
            }

            InformationSession session = this.getInformationSession(headerAuthorization);

            responseDto = deliverySearcher.handle(
                    new DeliverySearcherQuery(deliveryId, session.role(), session.entityCode())
            );

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryGetController@searchDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error DeliveryGetController@searchDelivery#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryGetController@searchDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
