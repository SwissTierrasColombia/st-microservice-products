package com.ai.st.microservice.quality.modules.feedbacks.application.create_feedback;

import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.exceptions.DeliveryProductNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.feedbacks.domain.Feedback;
import com.ai.st.microservice.quality.modules.feedbacks.domain.FeedbackComments;
import com.ai.st.microservice.quality.modules.feedbacks.domain.FeedbackDate;
import com.ai.st.microservice.quality.modules.feedbacks.domain.FeedbackURLAttachment;
import com.ai.st.microservice.quality.modules.feedbacks.domain.contracts.DeliveryProductFeedbackRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.ManagerCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.DateTime;
import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;

@Service
public final class FeedbackCreator implements CommandUseCase<FeedbackCreatorCommand> {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductFeedbackRepository feedbackRepository;
    private final DateTime dateTime;
    private final StoreFile storeFile;


    public FeedbackCreator(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository, DeliveryProductFeedbackRepository feedbackRepository, DateTime dateTime, StoreFile storeFile) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.feedbackRepository = feedbackRepository;
        this.dateTime = dateTime;
        this.storeFile = storeFile;
    }

    @Override
    public void handle(FeedbackCreatorCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        DeliveryProductId deliveryProductId = DeliveryProductId.fromValue(command.deliveryProductId());
        ManagerCode managerCode = ManagerCode.fromValue(command.managerCode());

        FeedbackComments feedbackComments = FeedbackComments.fromValue(command.feedback());
        FeedbackDate feedbackDate = FeedbackDate.fromValue(dateTime.now());

        verifyPermissions(deliveryId, deliveryProductId, managerCode);

        FeedbackURLAttachment urlAttachment = handleAttachment(deliveryId, command.attachment(), command.attachmentExtension());

        Feedback feedback = Feedback.create(
                feedbackComments,
                urlAttachment,
                feedbackDate
        );

        feedbackRepository.save(deliveryProductId, feedback);
    }

    private void verifyPermissions(DeliveryId deliveryId, DeliveryProductId deliveryProductId, ManagerCode managerCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify delivery product exists
        DeliveryProduct deliveryProduct = deliveryProductRepository.search(deliveryProductId);
        if (deliveryProduct == null) {
            throw new DeliveryProductNotFound();
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToManager(managerCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isInReview()) {
            throw new UnauthorizedToModifyDelivery("No se puede crear la retroalimentación, porque el estado de la entrega no lo permite.");
        }

        // verify status of the delivery product
        if (deliveryProduct.isPending()) {
            throw new UnauthorizedToModifyDelivery("No se puede crear la retroalimentación, porque el producto no ha sido aceptado o rechazado.");
        }

    }

    private FeedbackURLAttachment handleAttachment(DeliveryId deliveryId, byte[] attachment, String extension) {
        if (attachment != null) {
            String namespace = buildNamespace(deliveryId);
            String pathUrl = storeFile.storeFilePermanently(attachment, extension, namespace);
            return FeedbackURLAttachment.fromValue(pathUrl);
        }
        return null;
    }

    private String buildNamespace(DeliveryId deliveryId) {
        return String.format("/entregas/%d", deliveryId.value());
    }

}
