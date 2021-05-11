package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delivered_products_attachments", schema = "quality")
public final class DeliveredProductAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "observations", length = 1000)
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_product_id", referencedColumnName = "id")
    private DeliveredProductEntity deliveredProductEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id")
    private DeliveryEntity deliveryEntity;

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

    public DeliveredProductEntity getDeliveredProductEntity() {
        return deliveredProductEntity;
    }

    public void setDeliveredProductEntity(DeliveredProductEntity deliveredProductEntity) {
        this.deliveredProductEntity = deliveredProductEntity;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public DeliveryEntity getDeliveryEntity() {
        return deliveryEntity;
    }

    public void setDeliveryEntity(DeliveryEntity deliveryEntity) {
        this.deliveryEntity = deliveryEntity;
    }
}
