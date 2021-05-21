package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delivered_products_feedbacks", schema = "quality")
public final class DeliveredProductFeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "feedback", nullable = false, length = 1000)
    private String feedback;

    @Column(name = "attachment_url", length = 1000)
    private String attachmentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_product_id", referencedColumnName = "id", nullable = false)
    private DeliveredProductEntity deliveredProduct;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public DeliveredProductEntity getDeliveredProduct() {
        return deliveredProduct;
    }

    public void setDeliveredProduct(DeliveredProductEntity deliveredProduct) {
        this.deliveredProduct = deliveredProduct;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
