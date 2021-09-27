package com.ai.st.microservice.quality.modules.shared.domain;

public final class TaskXTFQualityControl {

    private final Long id;
    private final Long categoryId;
    private final Long userId;

    public TaskXTFQualityControl(Long id, Long categoryId, Long userId) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public Long id() {
        return id;
    }

    public Long categoryId() {
        return categoryId;
    }

    public Long userId() {
        return userId;
    }
}
