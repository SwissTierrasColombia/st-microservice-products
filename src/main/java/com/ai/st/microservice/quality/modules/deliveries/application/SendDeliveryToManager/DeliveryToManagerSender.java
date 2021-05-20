package com.ai.st.microservice.quality.modules.deliveries.application.SendDeliveryToManager;

import com.ai.st.microservice.quality.modules.deliveries.domain.Delivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryStatusId;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.contracts.DeliveryRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.DeliveryNotFound;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToChangeDeliveryStatusToDelivered;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToModifyDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToSearchDelivery;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.DeliveryProduct;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.domain.OperatorCode;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class DeliveryToManagerSender {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProductRepository deliveryProductRepository;
    private final ProductRepository productRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;

    public DeliveryToManagerSender(DeliveryRepository deliveryRepository, DeliveryProductRepository deliveryProductRepository,
                                   ProductRepository productRepository, DeliveryProductAttachmentRepository attachmentRepository) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryProductRepository = deliveryProductRepository;
        this.productRepository = productRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public void send(DeliveryToManagerSenderCommand command) {

        DeliveryId deliveryId = DeliveryId.fromValue(command.deliveryId());
        OperatorCode operatorCode = OperatorCode.fromValue(command.operatorCode());

        verifyPermissions(deliveryId, operatorCode);

        deliveryRepository.changeState(deliveryId, DeliveryStatusId.fromValue(DeliveryStatusId.DELIVERED));
    }

    private void verifyPermissions(DeliveryId deliveryId, OperatorCode operatorCode) {

        // verify delivery exists
        Delivery delivery = deliveryRepository.search(deliveryId);
        if (delivery == null) {
            throw new DeliveryNotFound();
        }

        // verify owner of the delivery
        if (!delivery.deliveryBelongToOperator(operatorCode)) {
            throw new UnauthorizedToSearchDelivery();
        }

        // verify status of the delivery
        if (!delivery.isDraft()) {
            throw new UnauthorizedToModifyDelivery("No se puede cambiar el estado de la entrega, porque el estado de la misma no lo permite.");
        }

        verifyIntegrityOfProducts(delivery.id());
    }

    private void verifyIntegrityOfProducts(DeliveryId deliveryId) {
        List<DeliveryProduct> deliveryProducts = deliveryProductRepository.findByDeliveryId(deliveryId);
        deliveryProducts.forEach(deliveryProduct -> {
            List<DeliveryProductAttachment> attachments = attachmentRepository.findByDeliveryProductId(deliveryProduct.deliveryProductId());
            if (productIsXTF(deliveryProduct.productId())) {
                verifyXTFAttachment(attachments);
            }
            verifyProductHaveMinimumOneAttachment(attachments);
        });
    }

    private boolean productIsXTF(ProductId productId) {
        Product product = productRepository.search(productId);
        if (product != null) {
            return product.isConfiguredAsXTF();
        }
        return false;
    }

    private void verifyProductHaveMinimumOneAttachment(List<DeliveryProductAttachment> attachments) {
        long count = attachments.size();
        if (count == 0) {
            throw new UnauthorizedToChangeDeliveryStatusToDelivered(
                    "No se puede enviar la entrega porque existe un producto que no tiene cargado ningún adjunto.");
        }
    }

    private void verifyXTFAttachment(List<DeliveryProductAttachment> attachments) {
        List<DeliveryProductAttachment> xtfAttachments =
                attachments.stream().filter(DeliveryProductAttachment::isXTF).collect(Collectors.toList());

        if (xtfAttachments.size() == 0) {
            throw new UnauthorizedToChangeDeliveryStatusToDelivered(
                    "No se puede enviar la entrega porque existe un producto que requiere se cargue al menos un archivo en formato XTF.");
        }

        long count = xtfAttachments.stream().filter(attachment -> !((DeliveryProductXTFAttachment) attachment).accepted()).count();
        if (count >= 1) {
            throw new UnauthorizedToChangeDeliveryStatusToDelivered(
                    "No se puede enviar la entrega porque existe un producto con un adjunto XTF cargado y no aceptado.");
        }
    }

}
