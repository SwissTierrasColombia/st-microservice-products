package com.ai.st.microservice.quality.modules.deliveries.application.verify_integrity_delivery;

import com.ai.st.microservice.quality.modules.deliveries.domain.DeliveryId;
import com.ai.st.microservice.quality.modules.attachments.domain.contracts.DeliveryProductAttachmentRepository;
import com.ai.st.microservice.quality.modules.delivered_products.domain.contracts.DeliveryProductRepository;
import com.ai.st.microservice.quality.modules.deliveries.domain.exceptions.UnauthorizedToChangeDeliveryStatusToDelivered;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProduct;
import com.ai.st.microservice.quality.modules.attachments.domain.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.attachments.domain.xtf.DeliveryProductXTFAttachment;
import com.ai.st.microservice.quality.modules.products.domain.Product;
import com.ai.st.microservice.quality.modules.products.domain.ProductId;
import com.ai.st.microservice.quality.modules.products.domain.contracts.ProductRepository;
import com.ai.st.microservice.quality.modules.shared.application.CommandUseCase;
import com.ai.st.microservice.quality.modules.shared.domain.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class IntegrityDeliveryChecker implements CommandUseCase<IntegrityDeliveryCheckerCommand> {

    private final DeliveryProductRepository deliveryProductRepository;
    private final DeliveryProductAttachmentRepository attachmentRepository;
    private final ProductRepository productRepository;

    public IntegrityDeliveryChecker(DeliveryProductRepository deliveryProductRepository,
                                    DeliveryProductAttachmentRepository attachmentRepository,
                                    ProductRepository productRepository) {
        this.deliveryProductRepository = deliveryProductRepository;
        this.attachmentRepository = attachmentRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void handle(IntegrityDeliveryCheckerCommand query) {

        DeliveryId deliveryId = DeliveryId.fromValue(query.deliveryId());

        verifyIntegrityOfProducts(deliveryId);
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
