package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.common.dto.operators.MicroserviceOperatorDto;
import com.ai.st.microservice.common.exceptions.DisconnectedMicroserviceException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries.DeliveriesFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.FindDeliveries.DeliveriesFinderQuery;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
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

import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryGetController.class);

    private final AdministrationBusiness administrationBusiness;
    private final ManagerBusiness managerBusiness;
    private final OperatorBusiness operatorBusiness;
    private final DeliveriesFinder deliveriesFinder;

    public DeliveryGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                 OperatorBusiness operatorBusiness, DeliveriesFinder deliveriesFinder) {
        this.administrationBusiness = administrationBusiness;
        this.managerBusiness = managerBusiness;
        this.operatorBusiness = operatorBusiness;
        this.deliveriesFinder = deliveriesFinder;
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
            @RequestParam(name = "state_id", required = false) Long stateId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            MicroserviceUserDto userDtoSession = administrationBusiness.getUserByToken(headerAuthorization);
            if (userDtoSession == null) {
                throw new DisconnectedMicroserviceException("Ha ocurrido un error consultando el usuario");
            }

            Roles role;
            Long entityCode;

            if (administrationBusiness.isManager(userDtoSession)) {
                MicroserviceManagerDto managerDto = managerBusiness.getManagerByUserCode(userDtoSession.getId());
                role = Roles.MANAGER;
                entityCode = managerDto.getId();
            } else {
                MicroserviceOperatorDto operatorDto = operatorBusiness.getOperatorByUserCode(userDtoSession.getId());
                role = Roles.OPERATOR;
                entityCode = operatorDto.getId();
            }

            responseDto = deliveriesFinder.finder(
                    new DeliveriesFinderQuery(
                            page,
                            limit,
                            stateId,
                            role,
                            entityCode));

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error DeliveryGetController@findDeliveries#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryGetController@findDeliveries#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

}
