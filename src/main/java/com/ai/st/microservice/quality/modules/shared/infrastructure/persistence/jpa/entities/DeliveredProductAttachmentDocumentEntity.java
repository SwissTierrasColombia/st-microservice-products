package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;

@Entity
@Table(name = "delivered_products_attachments_documents", schema = "quality")
public final class DeliveredProductAttachmentDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_product_attachment_id", referencedColumnName = "id", nullable = false)
    private DeliveredProductAttachmentEntity deliveredProductAttachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DeliveredProductAttachmentEntity getDeliveredProductAttachment() {
        return deliveredProductAttachment;
    }

    public void setDeliveredProductAttachment(DeliveredProductAttachmentEntity deliveredProductAttachment) {
        this.deliveredProductAttachment = deliveredProductAttachment;
    }
}
