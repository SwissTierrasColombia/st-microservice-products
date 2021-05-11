package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;

@Entity
@Table(name = "delivered_products_attachments_ftp", schema = "quality")
public final class DeliveredProductAttachmentFTP {

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
    @JoinColumn(name = "delivered_product_id", referencedColumnName = "id", nullable = false)
    private DeliveredProductAttachment deliveredProductAttachment;


}
