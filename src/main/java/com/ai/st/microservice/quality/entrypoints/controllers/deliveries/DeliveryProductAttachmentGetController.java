package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.AttachmentProductResponse;
import com.ai.st.microservice.quality.modules.deliveries.application.FindAttachmentsFromProduct.AttachmentsProductFinder;
import com.ai.st.microservice.quality.modules.deliveries.application.FindAttachmentsFromProduct.AttachmentsProductFinderQuery;
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
public final class DeliveryProductAttachmentGetController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductAttachmentGetController.class);

    private final AttachmentsProductFinder attachmentsProductFinder;

    public DeliveryProductAttachmentGetController(AdministrationBusiness administrationBusiness,
                                                  ManagerBusiness managerBusiness, OperatorBusiness operatorBusiness,
                                                  AttachmentsProductFinder attachmentsProductFinder) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.attachmentsProductFinder = attachmentsProductFinder;
    }

    @GetMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{deliveryProductId}/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get attachments from delivery product")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Attachments got", response = AttachmentProductResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Error Server", response = BasicResponseDto.class)})
    @ResponseBody
    public ResponseEntity<?> findAttachmentsFromProduct(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryProductId,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            responseDto = attachmentsProductFinder.find(
                    new AttachmentsProductFinderQuery(
                            deliveryId, deliveryProductId, session.role(), session.entityCode()
                    )
            );

            httpStatus = HttpStatus.OK;

        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentGetController@findAttachmentsFromProduct#Domain ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentGetController@findAttachmentsFromProduct#General ---> " + e.getMessage());
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
