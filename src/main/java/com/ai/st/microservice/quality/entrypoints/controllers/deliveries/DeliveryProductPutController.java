package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.AddProductToDelivery.AddProductToDeliveryCommand;
import com.ai.st.microservice.quality.modules.deliveries.application.AddProductToDelivery.ProductAssigner;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryProductPutController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductPutController.class);

    private final ProductAssigner assignProduct;

    public DeliveryProductPutController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                        OperatorBusiness operatorBusiness, ProductAssigner assignProduct) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.assignProduct = assignProduct;
    }

    @RequestMapping(value = "api/quality/v1/deliveries/{deliveryId}/products", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add product to delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product added"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> addProductToDelivery(@PathVariable Long deliveryId,
                                                  @RequestBody AddProductToDeliveryRequest request,
                                                  @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);

            Long productId = request.getProductId();
            validateProduct(productId);

            assignProduct.assign(
                    new AddProductToDeliveryCommand(
                            deliveryId,
                            productId,
                            session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductPutController@addProductToDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductPutController@addProductToDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductPutController@addProductToDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }


        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }

    private void validateDeliveryId(Long deliveryId) throws InputValidationException {
        if (deliveryId <= 0) {
            throw new InputValidationException("La entrega no es válida");
        }
    }

    private void validateProduct(Long productId) throws InputValidationException {
        if (productId <= 0) {
            throw new InputValidationException("El producto no es válido");
        }
    }


}

@ApiModel(value = "CreateDeliveryRequest")
final class AddProductToDeliveryRequest {

    @ApiModelProperty(notes = "Product ID")
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

