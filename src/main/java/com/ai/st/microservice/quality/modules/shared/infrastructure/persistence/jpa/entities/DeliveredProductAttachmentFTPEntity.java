package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;

@Entity
@Table(name = "delivered_products_attachments_ftp", schema = "quality")
public final class DeliveredProductAttachmentFTPEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "domain", nullable = false, length = 1000)
    private String domain;

    @Column(name = "port", nullable = false, length = 10)
    private String port;

    @Column(name = "username", nullable = false, length = 60)
    private String username;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_product_attachment_id", referencedColumnName = "id", nullable = false)
    private DeliveredProductAttachmentEntity deliveredProductAttachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeliveredProductAttachmentEntity getDeliveredProductAttachment() {
        return deliveredProductAttachment;
    }

    public void setDeliveredProductAttachment(DeliveredProductAttachmentEntity deliveredProductAttachment) {
        this.deliveredProductAttachment = deliveredProductAttachment;
    }
}
