package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delivered_products", schema = "quality")
public class DeliveredProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_product_status_id", referencedColumnName = "id", nullable = false)
    private DeliveryProductStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    private DeliveryEntity delivery;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public DeliveryProductStatusEntity getStatus() {
        return status;
    }

    public void setStatus(DeliveryProductStatusEntity status) {
        this.status = status;
    }

    public DeliveryEntity getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryEntity delivery) {
        this.delivery = delivery;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
