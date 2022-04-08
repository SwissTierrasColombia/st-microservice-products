package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.business.RoleBusiness;
import com.ai.st.microservice.common.clients.ManagerFeignClient;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerDto;
import com.ai.st.microservice.common.dto.managers.MicroserviceManagerUserDto;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerName;
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.ManagerMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.ManagerNotFound;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.MicroserviceUnreachable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<UserCode> getUsersByManager(ManagerCode managerCode) {

        List<UserCode> usersCodes;

        try {

            List<MicroserviceManagerUserDto> users = managerFeignClient.findUsersByManager(managerCode.value(),
                    new ArrayList<>(Collections.singletonList(RoleBusiness.SUB_ROLE_DIRECTOR_MANAGER)));

            usersCodes = users.stream().map(managerUserDto -> new UserCode(managerUserDto.getUserCode()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new MicroserviceUnreachable("manager");
        }

        return usersCodes;
    }

}
