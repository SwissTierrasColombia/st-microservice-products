package com.ai.st.microservice.quality.entrypoints.controllers.products;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.products.application.create_product.ProductCreator;
import com.ai.st.microservice.quality.modules.products.application.create_product.ProductCreatorCommand;
import com.ai.st.microservice.quality.modules.products.application.ProductResponse;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Api(value = "Manage products", tags = {"Products"})
@RestController
public final class ProductPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(ProductPostController.class);

    private final ProductCreator productCreator;

    public ProductPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                 OperatorBusiness operatorBusiness, ProductCreator productCreator) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.productCreator = productCreator;
    }

    @PostMapping(value = "api/quality/v1/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create product")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product created", response = ProductResponse.class),
            @ApiResponse(code = 500, message = "Error Server")})
    @ResponseBody
    public ResponseEntity<?> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            String name = request.getName();
            validateName(name);

            String description = request.getDescription();
            validateDescription(description);

            productCreator.handle(
                    new ProductCreatorCommand(
                            name, description, request.isXTF(), session.entityCode()
                    )
            );

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error ProductPostController@createProduct#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (DomainError e) {
            log.error("Error ProductPostController@createProduct#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error ProductPostController@createProduct#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        }

        return new ResponseEntity<>(responseDto, httpStatus);
    }

    private void validateName(String name) throws InputValidationException {
        if (name == null || name.isEmpty())
            throw new InputValidationException("El nombre del producto es requerido.");
    }

    private void validateDescription(String description) throws InputValidationException {
        if (description == null || description.isEmpty())
            throw new InputValidationException("La descripción del producto es requerida.");
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}

@ApiModel(value = "CreateProductRequest")
final class CreateProductRequest {

    @ApiModelProperty(notes = "Name")
    private String name;

    @ApiModelProperty(notes = "Description")
    private String description;

    @ApiModelProperty(notes = "is xtf?")
    private boolean isXTF;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isXTF() {
        return isXTF;
    }

    public void setXTF(boolean XTF) {
        isXTF = XTF;
    }
}
