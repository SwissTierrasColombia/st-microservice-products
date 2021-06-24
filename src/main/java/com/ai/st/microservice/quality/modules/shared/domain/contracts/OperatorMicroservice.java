package com.ai.st.microservice.quality.modules.shared.domain.contracts;

import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorName;

public interface OperatorMicroservice {

    OperatorName getOperatorName(OperatorCode operatorCode);

}
