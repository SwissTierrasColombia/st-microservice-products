package com.ai.st.microservice.quality.entrypoints.controllers.v1;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.DisconnectedMicroserviceException;
import com.ai.st.microservice.common.exceptions.InputValidationException;

import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.infrastructure.persistence.jpa.entities.DeliveryMethodEnum;
import com.ai.st.microservice.quality.modules.deliveries.application.CreateDelivery.DeliveryCreator;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;


@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryPostController.class);

    private final AdministrationBusiness administrationBusiness;
    private final DeliveryCreator deliveryCreator;

    public DeliveryPostController(AdministrationBusiness administrationBusiness, DeliveryCreator deliveryCreator) {
        this.administrationBusiness = administrationBusiness;
        this.deliveryCreator = deliveryCreator;
    }

    @RequestMapping(value = "api/quality/v1/deliveries", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Delivery done"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> createDelivery(@ModelAttribute CreateDeliveryRequest createDeliveryRequest,
                                            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            // user session
            MicroserviceUserDto userDtoSession = administrationBusiness.getUserByToken(headerAuthorization);
            if (userDtoSession == null) {
                throw new DisconnectedMicroserviceException("Ha ocurrido un error consultando el usuario");
            }

            // validation municipality
            Long municipalityId = createDeliveryRequest.getMunicipalityId();
            if (municipalityId == null || municipalityId <= 0) {
                throw new InputValidationException("El municipio es requerido.");
            }

            // validation observations
            String observations = createDeliveryRequest.getObservations();
            if (observations == null || observations.isEmpty()) {
                throw new InputValidationException("Las observaciones de la entrega son obligatorias.");
            }

            if (createDeliveryRequest.getProducts().size() == 0) {
                throw new InputValidationException("Se debe enviar al menos un producto en la entrega.");
            }

            for (CreateDeliveryProductDto s : createDeliveryRequest.getProducts()) {
                System.out.println("jhon " + s.getObservations());
                System.out.println("jhon " + s.getFile().getContentType());
                System.out.println("jhon " + s.getMethod());
            }

            System.out.println("hello");
            // this.deliveryCreator.create(null, );

            httpStatus = HttpStatus.CREATED;

        } catch (InputValidationException e) {
            log.error("Error DeliveryV1Controller@createDelivery#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (Exception e) {
            log.error("Error DeliveryV1Controller@createDelivery#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        }


        return new ResponseEntity<>(responseDto, httpStatus);
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}

@ApiModel(value = "CreateDeliveryRequest")
final class CreateDeliveryRequest {

    @ApiModelProperty(required = true, notes = "Municipality ID")
    private Long municipalityId;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    @ApiModelProperty(required = true, notes = "List of products")
    private List<CreateDeliveryProductDto> products;

    public Long getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(Long municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<CreateDeliveryProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<CreateDeliveryProductDto> products) {
        this.products = products;
    }
}

@ApiModel(value = "CreateDeliveryProductRequest")
final class CreateDeliveryProductDto {

    @ApiModelProperty(required = true, notes = "File")
    private MultipartFile file;

    @ApiModelProperty(required = true, notes = "Data")
    private String data;

    @ApiModelProperty(required = true, notes = "Observations")
    private String observations;

    @ApiModelProperty(required = true, notes = "Medio")
    private DeliveryMethodEnum method;

    @ApiModelProperty(required = true, notes = "Product code")
    private Long productId;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public DeliveryMethodEnum getMethod() {
        return method;
    }

    public void setMethod(DeliveryMethodEnum method) {
        this.method = method;
    }
}

