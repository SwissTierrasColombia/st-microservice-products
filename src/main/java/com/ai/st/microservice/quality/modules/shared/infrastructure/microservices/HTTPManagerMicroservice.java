package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.ManagerFeignClient;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerName;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ManagerNotFound;
import org.springframework.stereotype.Service;

@Service
public final class HTTPManagerMicroservice implements ManagerMicroservice {

    private final ManagerFeignClient managerFeignClient;

    public HTTPManagerMicroservice(ManagerFeignClient managerFeignClient) {
        this.managerFeignClient = managerFeignClient;
    }

    @Override
    public ManagerName getManagerName(ManagerCode managerCode) {
        MicroserviceManagerDto managerDto = managerFeignClient.findById(managerCode.value());

        if (managerDto == null) {
            throw new ManagerNotFound();
        }

        return ManagerName.fromValue(managerDto.getName());
    }

}
