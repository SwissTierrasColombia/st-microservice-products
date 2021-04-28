package com.ai.st.microservice.quality.infrastructure.v1.controllers;

import com.ai.st.microservice.quality.modules.shared.domain.DomainError;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

public abstract class ApiController {

    abstract public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping();

}
