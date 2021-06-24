package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.entities;

import com.ai.st.microservice.quality.modules.products.domain.ProductDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products", schema = "quality")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "manager_code", nullable = false)
    private Long managerCode;

    @Column(name = "is_xtf", nullable = false)
    private Boolean isXTF;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(Long managerCode) {
        this.managerCode = managerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getXTF() {
        return isXTF;
    }

    public void setXTF(Boolean XTF) {
        this.isXTF = XTF;
    }
}
