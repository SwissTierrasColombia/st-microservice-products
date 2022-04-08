package com.ai.st.microservice.quality.modules.shared.infrastructure.microservices;

import com.ai.st.microservice.common.clients.TaskFeignClient;
import com.ai.st.microservice.common.dto.tasks.*;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.shared.domain.DepartmentMunicipality;
import com.ai.st.microservice.quality.modules.shared.domain.TaskXTFQualityControl;
import com.ai.st.microservice.quality.modules.shared.domain.UserCode;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.TaskMicroservice;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.MicroserviceUnreachable;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public final class HTTPTaskMicroservice implements TaskMicroservice {

    private final Logger log = LoggerFactory.getLogger(HTTPTaskMicroservice.class);

    private final TaskFeignClient taskClient;

    public HTTPTaskMicroservice(TaskFeignClient taskClient) {
        this.taskClient = taskClient;
    }

    private static final Long TASK_CATEGORY_XTF_QUALITY_RULES = (long) 3;
    private static final Long TASK_STATE_ASSIGN = (long) 1;
    private static final Long TASK_STATE_STARTED = (long) 4;
    private static final int TASK_CATEGORY_XTF_QUALITY_RULES_DAYS_DEADLINE = 15;

    @Override
    public void createQualityRulesTask(DeliveryId deliveryId, DeliveryProductId deliveryProductId,
            DeliveryProductXTFAttachment attachment, DepartmentMunicipality departmentMunicipality,
            List<UserCode> users) {

        List<Long> categories = new ArrayList<>();
        categories.add(TASK_CATEGORY_XTF_QUALITY_RULES);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, TASK_CATEGORY_XTF_QUALITY_RULES_DAYS_DEADLINE);
        String deadline = sdf.format(cal.getTime());

        String name = String.format("%s (%s)", departmentMunicipality.municipality(),
                departmentMunicipality.department());
        String description = "Realizar control de calidad XTF (LevCAT)";

        List<MicroserviceCreateTaskMetadataDto> metadata = new ArrayList<>();
        MicroserviceCreateTaskMetadataDto metadataRequest = new MicroserviceCreateTaskMetadataDto();
        metadataRequest.setKey("attachment");
        List<MicroserviceCreateTaskPropertyDto> listPropertiesRequest = new ArrayList<>();
        listPropertiesRequest.add(new MicroserviceCreateTaskPropertyDto("version", attachment.version().value()));
        listPropertiesRequest.add(new MicroserviceCreateTaskPropertyDto("deliveryId", deliveryId.value().toString()));
        listPropertiesRequest
                .add(new MicroserviceCreateTaskPropertyDto("deliveryProductId", deliveryProductId.value().toString()));
        listPropertiesRequest.add(new MicroserviceCreateTaskPropertyDto("attachmentId",
                attachment.deliveryProductAttachmentId().value().toString()));
        metadataRequest.setProperties(listPropertiesRequest);
        metadata.add(metadataRequest);

        List<MicroserviceCreateTaskStepDto> steps = new ArrayList<>();

        try {

            MicroserviceCreateTaskDto createTask = new MicroserviceCreateTaskDto();

            createTask.setCategories(categories);
            createTask.setDeadline(deadline);
            createTask.setDescription(description);
            createTask.setMetadata(metadata);
            createTask.setName(name);
            createTask.setUsers(users.stream().map(UserCode::value).collect(Collectors.toList()));
            createTask.setSteps(steps);

            taskClient.createTask(createTask);

        } catch (Exception e) {
            String messageError = String.format("Error creando tarea de levantamiento catastral: %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new MicroserviceUnreachable("tasks");
        }

    }

    @Override
    public TaskXTFQualityControl findQualityProcessTask(DeliveryProductXTFAttachment attachment) {

        try {

            List<Long> taskStates = new ArrayList<>(List.of(TASK_STATE_STARTED, TASK_STATE_ASSIGN));
            List<Long> taskCategories = new ArrayList<>(List.of(TASK_CATEGORY_XTF_QUALITY_RULES));

            List<MicroserviceTaskDto> responseTasksDto = taskClient.findByStateAndCategory(taskStates, taskCategories);

            for (MicroserviceTaskDto taskDto : responseTasksDto) {

                MicroserviceTaskMetadataDto metadataRequest = taskDto.getMetadata().stream()
                        .filter(meta -> meta.getKey().equalsIgnoreCase("attachment")).findAny().orElse(null);

                if (metadataRequest != null) {

                    MicroserviceTaskMetadataPropertyDto propertyAttachmentId = metadataRequest.getProperties().stream()
                            .filter(p -> p.getKey().equalsIgnoreCase("attachmentId")).findAny().orElse(null);

                    if (propertyAttachmentId != null) {
                        Long taskAttachmentId = Long.parseLong(propertyAttachmentId.getValue());
                        if (taskAttachmentId.equals(attachment.deliveryProductAttachmentId().value())) {
                            MicroserviceTaskMemberDto userDto = taskDto.getMembers().stream().findFirst().orElse(null);
                            if (userDto != null) {
                                return new TaskXTFQualityControl(taskDto.getId(), TASK_CATEGORY_XTF_QUALITY_RULES,
                                        userDto.getMemberCode());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            String messageError = String.format(
                    "Error consultando la tarea de levantamiento catastral del adjunto %s: %s",
                    attachment.uuid().value(), e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            return null;
        }

        return null;
    }
}
