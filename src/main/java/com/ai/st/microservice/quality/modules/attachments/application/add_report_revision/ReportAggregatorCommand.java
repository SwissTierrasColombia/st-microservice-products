package com.ai.st.microservice.quality.modules.attachments.application.add_report_revision;

import com.ai.st.microservice.quality.modules.shared.application.Command;

public final class ReportAggregatorCommand implements Command {

    private final Long deliveryId;
    private final Long deliveryProductId;
    private final Long attachmentId;
    private final Long managerCode;
    private final Long userCode;
    private final boolean overwrite;
    private final byte[] bytesFile;
    private final String extensionFile;

    public ReportAggregatorCommand(Long deliveryId, Long deliveryProductId, Long attachmentId, Long managerCode,
                                   Long userCode, boolean overwrite, byte[] bytesFile, String extensionFile) {
        this.deliveryId = deliveryId;
        this.deliveryProductId = deliveryProductId;
        this.attachmentId = attachmentId;
        this.managerCode = managerCode;
        this.userCode = userCode;
        this.overwrite = overwrite;
        this.bytesFile = bytesFile;
        this.extensionFile = extensionFile;
    }

    public Long deliveryId() {
        return deliveryId;
    }

    public Long deliveryProductId() {
        return deliveryProductId;
    }

    public Long attachmentId() {
        return attachmentId;
    }

    public Long managerCode() {
        return managerCode;
    }

    public byte[] bytesFile() {
        return bytesFile;
    }

    public String extensionFile() {
        return extensionFile;
    }

    public boolean overwrite() {
        return overwrite;
    }

    public Long userCode() {
        return userCode;
    }
}
