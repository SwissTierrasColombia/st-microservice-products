package com.ai.st.microservice.quality.infrastructure.persistence.entities;

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
    @JoinColumn(name = "product_status_id", referencedColumnName = "id", nullable = false)
    private DeliveryProductStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    private DeliveryEntity delivery;

    @Column(name = "observations", length = 1000)
    private String observations;

    @Column(name = "medio", nullable = false, length = 50)
    @Enumerated(value = EnumType.STRING)
    private DeliveryMethodEnum medio;

    @Column(name = "data", nullable = false, length = 1000)
    private String data;

    @Column(name = "downloaded")
    private Boolean downloaded;

    @Column(name = "downloaded_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadedAt;

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

    public DeliveryMethodEnum getMedio() {
        return medio;
    }

    public void setMedio(DeliveryMethodEnum medio) {
        this.medio = medio;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }

    public Date getDownloadedAt() {
        return downloadedAt;
    }

    public void setDownloadedAt(Date downloadedAt) {
        this.downloadedAt = downloadedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
