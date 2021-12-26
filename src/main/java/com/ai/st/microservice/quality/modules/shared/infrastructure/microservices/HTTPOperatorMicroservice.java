package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.OperatorFeignClient;
import com.ai.st.microservice.common.dto.operators.MicroserviceOperatorDto;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorName;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.OperatorMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.OperatorNotFound;
import org.springframework.stereotype.Service;

@Service
public final class HTTPOperatorMicroservice implements OperatorMicroservice {

    private final OperatorFeignClient operatorFeignClient;

    public HTTPOperatorMicroservice(OperatorFeignClient operatorFeignClient) {
        this.operatorFeignClient = operatorFeignClient;
    }

    @Override
    public OperatorName getOperatorName(OperatorCode operatorCode) {

        MicroserviceOperatorDto operatorDto = operatorFeignClient.findById(operatorCode.value());
        if (operatorDto == null) {
            throw new OperatorNotFound();
        }

        return OperatorName.fromValue(operatorDto.getName());
    }

}
