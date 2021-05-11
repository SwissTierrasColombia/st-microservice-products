package com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.ftp;

import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachment;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentId;
import com.ai.st.microservice.quality.modules.deliveries.domain.products.attachments.DeliveryProductAttachmentObservations;

public final class DeliveryProductFTPAttachment extends DeliveryProductAttachment {

    private final FTPDomain domain;
    private final FTPPort port;
    private final FTPUsername username;
    private final FTPPassword password;

    public DeliveryProductFTPAttachment(DeliveryProductAttachmentId id, DeliveryProductAttachmentObservations observations,
                                        FTPDomain domain, FTPPort port, FTPUsername username, FTPPassword password) {
        super(id, observations);
        this.domain = domain;
        this.port = port;
        this.username = username;
        this.password = password;
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
