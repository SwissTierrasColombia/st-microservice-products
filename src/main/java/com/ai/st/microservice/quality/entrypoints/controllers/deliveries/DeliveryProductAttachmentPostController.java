package com.ai.st.microservice.quality.entrypoints.controllers.deliveries;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.general.BasicResponseDto;
import com.ai.st.microservice.common.exceptions.InputValidationException;
import com.ai.st.microservice.quality.entrypoints.controllers.ApiController;
import com.ai.st.microservice.quality.modules.deliveries.application.AddAttachmentToProduct.AttachmentAssigner;
import com.ai.st.microservice.quality.modules.deliveries.application.AddAttachmentToProduct.AttachmentAssignerCommand;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.AddingAttachmentToProductFailed;
import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.CompressorFile;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Api(value = "Manage Deliveries", tags = {"Deliveries"})
@RestController
public final class DeliveryProductAttachmentPostController extends ApiController {

    private final Logger log = LoggerFactory.getLogger(DeliveryProductAttachmentPostController.class);

    private final AttachmentAssigner attachmentAssigner;
    private final StoreFile storeFile;
    private final CompressorFile compressorFile;

    public DeliveryProductAttachmentPostController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
                                                   OperatorBusiness operatorBusiness, AttachmentAssigner attachmentAssigner,
                                                   StoreFile storeFile, CompressorFile compressorFile) {
        super(administrationBusiness, managerBusiness, operatorBusiness);
        this.attachmentAssigner = attachmentAssigner;
        this.storeFile = storeFile;
        this.compressorFile = compressorFile;
    }

    @PostMapping(value = "api/quality/v1/deliveries/{deliveryId}/products/{productId}/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add attachment to product from delivery")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Delivery created"),
            @ApiResponse(code = 500, message = "Error Server", response = String.class)})
    @ResponseBody
    public ResponseEntity<?> addAttachmentToProduct(
            @PathVariable Long deliveryId,
            @PathVariable Long productId,
            @ModelAttribute AddAttachmentToProductRequest request,
            @RequestHeader("authorization") String headerAuthorization) {

        HttpStatus httpStatus;
        Object responseDto = null;

        try {

            InformationSession session = this.getInformationSession(headerAuthorization);

            validateDeliveryId(deliveryId);
            validateProduct(productId);

            String observations = request.getObservations();

            AttachmentAssignerCommand.Attachment attachment;
            if (request.getXtf() != null) {
                attachment = validateXTFAttachment(observations, request.getXtf());
            } else if (request.getFtp() != null) {
                attachment = validateFTPAttachment(observations, request.getFtp());
            } else if (request.getDocument() != null) {
                attachment = validateDocumentAttachment(observations, request.getDocument());
            } else {
                throw new InputValidationException("Se debe enviar un tipo de adjunto (ftp,documento,xtf).");
            }

            attachmentAssigner.assign(new AttachmentAssignerCommand(
                    deliveryId, productId, session.entityCode(), attachment
            ));

            httpStatus = HttpStatus.OK;

        } catch (InputValidationException e) {
            log.error("Error DeliveryProductAttachmentPostController@addAttachmentToProduct#Validation ---> " + e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
            responseDto = new BasicResponseDto(e.getMessage(), 1);
        } catch (DomainError e) {
            log.error("Error DeliveryProductAttachmentPostController@addAttachmentToProduct#Domain ---> " + e.errorMessage());
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            responseDto = new BasicResponseDto(e.errorMessage(), 2);
        } catch (IOException e) {
            log.error("Error DeliveryProductAttachmentPostController@addAttachmentToProduct#Files ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 3);
        } catch (Exception e) {
            log.error("Error DeliveryProductAttachmentPostController@addAttachmentToProduct#General ---> " + e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            responseDto = new BasicResponseDto(e.getMessage(), 4);
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

    private AttachmentAssignerCommand.XTFAttachment validateXTFAttachment(String observations, AddXTFAttachmentRequest request)
            throws InputValidationException, IOException {
        MultipartFile file = request.getAttachment();
        if (file == null) {
            throw new InputValidationException("El archivo XTF es requerido.");
        }
        String extension = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
        boolean isZip = extension.equalsIgnoreCase("zip");
        if (!isZip) {
            throw new InputValidationException("El archivo debe cargarse en formato zip");
        }

        String temporalFilePath = storeFile.storeFileTemporarily(file.getBytes(), extension);
        int countEntries = compressorFile.countEntries(temporalFilePath);
        if (countEntries != 1) {
            throw new InputValidationException("El comprimido sólo debe contener un archivo y en formato XTF.");
        }

        boolean filePresent = compressorFile.checkIfFileIsPresent(temporalFilePath, "xtf");
        if (!filePresent) {
            throw new InputValidationException("El comprimido no contiene un archivo en formato XTF.");
        }

        storeFile.deleteFile(temporalFilePath);

        return new AttachmentAssignerCommand.XTFAttachment(
                observations, file.getBytes(), extension);
    }

    private AttachmentAssignerCommand.FTPAttachment validateFTPAttachment(String observations, AddFTPAttachmentRequest request)
            throws InputValidationException {
        if (request.getDomain() == null || request.getDomain().isEmpty()) {
            throw new InputValidationException("La URL del servidor FTP es requerido.");
        }
        if (request.getPort() == null || request.getPort().isEmpty()) {
            throw new InputValidationException("El puerto del servidor FTP es requerido.");
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new InputValidationException("El nombre de usuario del servidor FTP es requerido.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new InputValidationException("La clave del servidor FTP es requerido.");
        }

        return new AttachmentAssignerCommand.FTPAttachment(
                observations, request.getDomain(), request.getPort(), request.getUsername(), request.getPassword()
        );
    }

    private AttachmentAssignerCommand.DocumentAttachment validateDocumentAttachment(String observations, AddDocumentAttachmentRequest request)
            throws InputValidationException, IOException {
        MultipartFile file = request.getAttachment();
        if (file == null) {
            throw new InputValidationException("El archivo es requerido.");
        }
        String extension = Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()));
        return new AttachmentAssignerCommand.DocumentAttachment(
                observations, file.getBytes(), extension
        );
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return null;
    }
}

@ApiModel(value = "AddAttachmentToProductRequest")
final class AddAttachmentToProductRequest {

    @ApiModelProperty(required = true, notes = "Attachment")
    private String observations;

    private AddXTFAttachmentRequest xtf;
    private AddFTPAttachmentRequest ftp;
    private AddDocumentAttachmentRequest document;

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public AddXTFAttachmentRequest getXtf() {
        return xtf;
    }

    public void setXtf(AddXTFAttachmentRequest xtf) {
        this.xtf = xtf;
    }

    public AddFTPAttachmentRequest getFtp() {
        return ftp;
    }

    public void setFtp(AddFTPAttachmentRequest ftp) {
        this.ftp = ftp;
    }

    public AddDocumentAttachmentRequest getDocument() {
        return document;
    }

    public void setDocument(AddDocumentAttachmentRequest document) {
        this.document = document;
    }
}

final class AddXTFAttachmentRequest {

    @ApiModelProperty(required = true, notes = "Attachment")
    private MultipartFile attachment;

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}

final class AddFTPAttachmentRequest {

    @ApiModelProperty(required = true, notes = "Domain")
    private String domain;

    @ApiModelProperty(required = true, notes = "Port")
    private String port;

    @ApiModelProperty(required = true, notes = "Username")
    private String username;

    @ApiModelProperty(required = true, notes = "Password")
    private String password;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

final class AddDocumentAttachmentRequest {

    @ApiModelProperty(required = true, notes = "Attachment")
    private MultipartFile attachment;

    public MultipartFile getAttachment() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}
