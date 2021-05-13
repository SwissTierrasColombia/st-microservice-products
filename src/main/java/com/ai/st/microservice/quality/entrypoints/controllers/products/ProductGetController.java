package com.ai.st.microservice.quality.entrypoints.controllers.products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.Roles;
import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.products.application.FindProductsFromManager.ManagerProductsFinder;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
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

@RestController
public final class ProductGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(ProductGetController.class);

    private final ManagerProductsFinder productsFinder;

    public ProductGetController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                OperatorBusiness operatorBusiness, ManagerProductsFinder productsFinder) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.productsFinder = productsFinder;
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

    @RequestMapping(value = "api/quality/v1/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search products from manager")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Products", responseContainer = "List", response = ProductResponse.class),
            @ApiResponse(code = 500, message = "Error Server")})
    @ResponseBody
    public ResponseEntity<?> searchProductsFromManager(@RequestParam(name = "manager", required = false) Long managerCode,
                                                       @RequestHeader("authorization") String headerAuthorization) {
        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            if (session.role().equals(Roles.MANAGER)) {
                managerCode = session.entityCode();
            }

            if (managerCode == null) {
                throw new InputValidationException("El gestor es requerido.");
            }

            responseDto = productsFinder.finder(managerCode);
            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error ProductGetController@searchProductsFromManager#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error ProductGetController@searchProductsFromManager#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error ProductGetController@searchProductsFromManager#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

}
