package com.ai.st.microservice.quality.modules.shared.application;

import java.util.List;

public final class PageableResponse<T> {

    private final List<T> items;
    private final int currentPage;
    private final int numberOfElements;
    private final long totalElements;
    private final int totalPages;
    private final int size;

    public PageableResponse(List<T> items, int currentPage, int numberOfElements, long totalElements, int totalPages, int size) {
        this.currentPage = currentPage;
        this.numberOfElements = numberOfElements;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.items = items;
    }

    public int currentPage() {
        return currentPage;
    }

    public int numberOfElements() {
        return numberOfElements;
    }

    public long totalElements() {
        return totalElements;
    }

    public int totalPages() {
        return totalPages;
    }

    public int size() {
        return size;
    }

    public List<T> items() {
        return items;
    }

}
