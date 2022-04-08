package com.ai.st.microservice.quality.entrypoints.controllers;

import com.ai.st.microservice.common.business.AdministrationBusiness;
import com.ai.st.microservice.common.business.ManagerBusiness;
import com.ai.st.microservice.common.business.OperatorBusiness;
import com.ai.st.microservice.common.dto.administration.MicroserviceUserDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.common.dto.operators.MicroserviceOperatorDto;
import com.ai.st.microservice.common.exceptions.DisconnectedMicroserviceException;
import com.ai.st.microservice.quality.modules.shared.application.Roles;

import com.google.common.io.Files;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;

public abstract class ApiController {

    protected final AdministrationBusiness administrationBusiness;
    protected final ManagerBusiness managerBusiness;
    protected final OperatorBusiness operatorBusiness;

    public ApiController(AdministrationBusiness administrationBusiness, ManagerBusiness managerBusiness,
            OperatorBusiness operatorBusiness) {
        this.administrationBusiness = administrationBusiness;
        this.managerBusiness = managerBusiness;
        this.operatorBusiness = operatorBusiness;
    }

    protected MicroserviceUserDto getUserSession(String headerAuthorization) throws DisconnectedMicroserviceException {
        MicroserviceUserDto userDtoSession = administrationBusiness.getUserByToken(headerAuthorization);
        if (userDtoSession == null) {
            throw new DisconnectedMicroserviceException("Ha ocurrido un error consultando el usuario");
        }
        return userDtoSession;
    }

    protected InformationSession getInformationSession(String headerAuthorization)
            throws DisconnectedMicroserviceException {
        MicroserviceUserDto userDtoSession = this.getUserSession(headerAuthorization);
        if (administrationBusiness.isManager(userDtoSession)) {
            MicroserviceManagerDto managerDto = managerBusiness.getManagerByUserCode(userDtoSession.getId());
            return new InformationSession(Roles.MANAGER, managerDto.getId(), userDtoSession.getId(),
                    managerDto.getName());
        } else if (administrationBusiness.isOperator(userDtoSession)) {
            MicroserviceOperatorDto operatorDto = operatorBusiness.getOperatorByUserCode(userDtoSession.getId());
            return new InformationSession(Roles.OPERATOR, operatorDto.getId(), userDtoSession.getId(),
                    operatorDto.getName());
        }
        throw new RuntimeException("User information not found");
    }

    protected ResponseEntity<?> responseFile(File file, MediaType mediaType, InputStreamResource resource) {
        String extension = Files.getFileExtension(file.getName());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(mediaType).contentLength(file.length()).header("extension", extension)
                .header("filename", file.getName() + extension).body(resource);
    }

    public final static class InformationSession {

        private final Roles role;
        private final Long entityCode;
        private final Long userCode;
        private final String entityName;

        public InformationSession(Roles role, Long entityCode, Long userCode, String entityName) {
            this.role = role;
            this.entityCode = entityCode;
            this.userCode = userCode;
            this.entityName = entityName;
        }

        public Roles role() {
            return role;
        }

        public Long entityCode() {
            return entityCode;
        }

        public Long userCode() {
            return userCode;
        }

        public String entityName() {
            return entityName;
        }
    }

}
