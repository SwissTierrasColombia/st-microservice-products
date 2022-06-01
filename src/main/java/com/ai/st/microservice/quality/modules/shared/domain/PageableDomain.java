package com.ai.st.microservice.quality.modules.shared.domain;

import java.util.List;
import java.util.Optional;

public final class PageableDomain<T> {

    private final List<T> items;
    private final Optional<Integer> currentPage;
    private final Optional<Integer> numberOfElements;
    private final Optional<Long> totalElements;
    private final Optional<Integer> totalPages;
    private final Optional<Integer> size;

    public PageableDomain(List<T> items, Optional<Integer> currentPage, Optional<Integer> numberOfElements,
            Optional<Long> totalElements, Optional<Integer> totalPages, Optional<Integer> size) {
        this.currentPage = currentPage;
        this.numberOfElements = numberOfElements;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.items = items;
    }

    public Optional<Integer> currentPage() {
        return currentPage;
    }

    public Optional<Integer> numberOfElements() {
        return numberOfElements;
    }

    public Optional<Long> totalElements() {
        return totalElements;
    }

    public Optional<Integer> totalPages() {
        return totalPages;
    }

    public Optional<Integer> size() {
        return size;
    }

    public List<T> items() {
        return items;
    }

}
