package com.ai.st.microservice.quality.modules.shared.domain.contracts;

import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.shared.domain.DepartmentMunicipality;
import com.ai.st.microservice.quality.modules.shared.domain.TaskXTFQualityControl;
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;

import java.util.List;

public interface TaskMicroservice {

    void createQualityRulesTask(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
            DeliveryProductXTFAttachment attachment, DepartmentMunicipality departmentMunicipality,
            List<UserCode> users);

    TaskXTFQualityControl findQualityProcessTask(DeliveryProductXTFAttachment attachment);

}
