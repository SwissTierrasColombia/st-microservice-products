package com.ai.st.microservice.quality.modules.shared.domain.contracts;

import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerName;
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;

import java.util.List;

public interface ManagerMicroservice {

    ManagerName getManagerName(ManagerCode managerCode);

    List<UserCode> getUsersByManager(ManagerCode managerCode);

}
