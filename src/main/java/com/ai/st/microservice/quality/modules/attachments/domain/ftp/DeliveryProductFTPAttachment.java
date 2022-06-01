package com.ai.st.microservice.quality.modules.attachments.domain.ftp;

import com.ai.st.microservice.quality.modules.attachments.domain.*;
import com.ai.st.microservice.quality.modules.delivered_products.domain.DeliveryProductId;

import java.util.Date;

public final class DeliveryProductFTPAttachment extends DeliveryProductAttachment {

    private final FTPDomain domain;
    private final FTPPort port;
    private final FTPUsername username;
    private final FTPPassword password;

    public DeliveryProductFTPAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentUUID uuid,
            DeliveryProductAttachmentObservations observations, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentDate deliveryProductAttachmentDate, FTPDomain domain, FTPPort port,
            FTPUsername username, FTPPassword password) {
        super(id, uuid, observations, deliveryProductId, deliveryProductAttachmentDate);
        this.domain = domain;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public static DeliveryProductFTPAttachment create(DeliveryProductAttachmentUUID uuid,
            DeliveryProductAttachmentObservations observations, DeliveryProductId deliveryProductId,
            DeliveryProductAttachmentDate deliveryProductAttachmentDate, FTPDomain domain, FTPPort port,
            FTPUsername username, FTPPassword password) {
        return new DeliveryProductFTPAttachment(null, uuid, observations, deliveryProductId,
                deliveryProductAttachmentDate, domain, port, username, password);
    }

    public static DeliveryProductFTPAttachment fromPrimitives(Long id, String uuid, String observations,
            Long deliveryProductId, Date date, String domain, String port, String username, String password) {
        return new DeliveryProductFTPAttachment(new DeliveryProductAttachmentId(id),
                new DeliveryProductAttachmentUUID(uuid), new DeliveryProductAttachmentObservations(observations),
                new DeliveryProductId(deliveryProductId), new DeliveryProductAttachmentDate(date),
                new FTPDomain(domain), new FTPPort(port), new FTPUsername(username), new FTPPassword(password));
    }

    public FTPDomain domain() {
        return domain;
    }

    public FTPPort port() {
        return port;
    }

    public FTPUsername username() {
        return username;
    }

    public FTPPassword password() {
        return password;
    }

}
