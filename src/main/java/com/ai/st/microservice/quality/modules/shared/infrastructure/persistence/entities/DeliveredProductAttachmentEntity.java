package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delivered_products_attachments", schema = "quality")
public final class DeliveredProductAttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", length = 36)
    private String uuid;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "observations", length = 1000)
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_product_id", referencedColumnName = "id", nullable = false)
    private DeliveredProductEntity deliveredProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public DeliveredProductEntity getDeliveredProduct() {
        return deliveredProduct;
    }

    public void setDeliveredProduct(DeliveredProductEntity deliveredProduct) {
        this.deliveredProduct = deliveredProduct;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
