package com.ai.st.microservice.quality.modules.shared.infrastructure.persistence.jpa.entities;

import javax.persistence.*;

@Entity
@Table(name = "delivered_products_attachments_xtf", schema = "quality")
public final class DeliveredProductAttachmentXTF {

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivered_product_id", referencedColumnName = "id", nullable = false)
    private DeliveredProductAttachment deliveredProductAttachment;

}
