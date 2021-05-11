package com.ai.st.microservice.quality.modules.shared.domain.contracts;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.MunicipalityCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;

public interface WorkspaceMicroservice {

    boolean verifyOperatorBelongToManager(OperatorCode operatorCode, ManagerCode managerCode,
                                          MunicipalityCode municipalityCode);

}
