package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "delivered_products_attachments_xtf", schema = "quality")
public final class DeliveredProductAttachmentXTFEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private StatusXTFEnum status;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public DeliveredProductAttachmentEntity getDeliveredProductAttachment() {
        return deliveredProductAttachment;
    }

    public void setDeliveredProductAttachment(DeliveredProductAttachmentEntity deliveredProductAttachment) {
        this.deliveredProductAttachment = deliveredProductAttachment;
    }

    public StatusXTFEnum getStatus() {
        return status;
    }

    public void setStatus(StatusXTFEnum status) {
        this.status = status;
    }
}
