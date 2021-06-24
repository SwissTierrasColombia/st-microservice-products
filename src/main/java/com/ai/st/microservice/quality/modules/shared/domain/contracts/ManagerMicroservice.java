package com.ai.st.microservice.quality.modules.shared.domain.contracts;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerName;

public interface ManagerMicroservice {

    ManagerName getManagerName(ManagerCode managerCode);

}
