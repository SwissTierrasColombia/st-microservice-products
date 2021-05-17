package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.WorkspaceFeignClient;
import com.ai.st.microservice.common.dto.workspaces.MicroserviceWorkspaceOperatorDto;
import com.ai.st.microservice.quality.modules.products.domain.exceptions.MicroserviceUnreachable;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.MunicipalityCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.WorkspaceMicroservice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class HTTPWorkspaceMicroservice implements WorkspaceMicroservice {

    private final WorkspaceFeignClient workspaceFeignClient;

    public HTTPWorkspaceMicroservice(WorkspaceFeignClient workspaceFeignClient) {
        this.workspaceFeignClient = workspaceFeignClient;
    }

    @Override
    public boolean verifyOperatorBelongToManager(OperatorCode operatorCode, ManagerCode managerCode, MunicipalityCode municipalityCode) {

        List<MicroserviceWorkspaceOperatorDto> workspacesOperators;
        try {
            workspacesOperators = workspaceFeignClient.findWorkspacesByOperator(operatorCode.value());

            int size = (int) workspacesOperators.stream().filter(microserviceWorkspaceDto ->
                    microserviceWorkspaceDto.getMunicipality().getCode().equals(municipalityCode.value())).collect(Collectors.toList())
                    .stream().filter(microserviceWorkspaceOperator -> microserviceWorkspaceOperator.getManagerCode().equals(managerCode.value())).count();

            return (size > 0);
        } catch (Exception e) {
            throw new MicroserviceUnreachable("workspaces");
        }
    }

}
