package com.ai.st.microservice.quality.entrypoints.controllers.delivered_products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.delivered_products.application.add_product_to_delivery.DeliveryProductAssignerCommand;
import com.ai.st.microservice.quality.modules.delivered_products.application.add_product_to_delivery.DeliveryProductAssigner;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

import io.swagger.annotations.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Manage Deliveries", tags = { "Deliveries" })
@RestController
public final class DeliveryProductPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductPostController.class);

    private final DeliveryProductAssigner deliveryProductAssigner;

    public DeliveryProductPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            OperatorBusiness operatorBusiness, DeliveryProductAssigner assignProduct) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.deliveryProductAssigner = assignProduct;
    }

    @PostMapping(value = "api/quality/v1/deliveries/{deliveryId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add product to delivery")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Product added"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class) })
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

            deliveryProductAssigner
                    .handle(new DeliveryProductAssignerCommand(deliveryId, productId, session.entityCode()));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductPostController@addProductToDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductPostController@addProductToDelivery#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductPostController@addProductToDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
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
