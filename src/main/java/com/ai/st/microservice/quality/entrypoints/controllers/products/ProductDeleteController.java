package com.ai.st.microservice.quality.entrypoints.controllers.products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.products.application.remove_product.ProductRemover;
import com.ai.st.microservice.quality.modules.products.application.remove_product.ProductRemoverCommand;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;

import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.TracingKeyword;
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

@Api(value = "Manage products", tags = { "Products" })
@RestController
public final class ProductDeleteController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(ProductDeleteController.class);

    private final ProductRemover productRemover;

    public ProductDeleteController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            OperatorBusiness operatorBusiness, ProductRemover productRemover) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.productRemover = productRemover;
    }

    @DeleteMapping(value = "api/quality/v1/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete product")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "Products"),
            @ApiResponse(code = 500, message = "Error Server") })
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            SCMTracing.setTransactionName("deleteProduct");
            SCMTracing.addCustomParameter(TracingKeyword.AUTHORIZATION_HEADER, headerAuthorization);

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateProductId(productId);

            productRemover.handle(new ProductRemoverCommand(productId, session.entityCode()));

            httpStatus = HttpStatus.NO_CONTENT;

        } catch (InputValidationException e) {
            log.error("Error ProductDeleteController@deleteProduct#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (DomainError e) {
            log.error("Error ProductDeleteController@deleteProduct#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage());
            SCMTracing.sendError(e.getMessage());
        } catch (Exception e) {
            log.error("Error ProductDeleteController@deleteProduct#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage());
            SCMTracing.sendError(e.getMessage());
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateProductId(Long productId) throws InputValidationException {
        if (productId == null || productId <= 0)
            throw new InputValidationException("El nombre del producto es requerido.");
    }

}
