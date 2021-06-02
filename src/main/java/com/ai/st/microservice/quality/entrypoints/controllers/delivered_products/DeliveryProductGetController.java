package com.ai.st.microservice.quality.entrypoints.controllers.delivered_products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.delivered_products.application.find_products_from_delivery.DeliveryProductsFinder;
import com.ai.st.microservice.quality.modules.delivered_products.application.find_products_from_delivery.DeliveryProductsFinderQuery;
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

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryProductGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductGetController.class);

    private final DeliveryProductsFinder deliveryProductsFinder;

    public DeliveryProductGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                        OperatorBusiness operatorBusiness, DeliveryProductsFinder deliveryProductsFinder) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryProductsFinder = deliveryProductsFinder;
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get deliveries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deliveries got", response = PageableResponse.class),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> findDeliveryProducts(
            @PathVariable Long deliveryId,
            @RequestHeader("authorization") String headerAuthorization
    ) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            responseDto = deliveryProductsFinder.handle(
                    new DeliveryProductsFinderQuery(
                            deliveryId, session.role(), session.entityCode())).list();

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error DeliveryProductGetController@findDeliveryProducts#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductGetController@findDeliveryProducts#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
