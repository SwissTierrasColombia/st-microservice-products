package com.ai.st.microservice.quality.modules.shared.domain.criteria;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Criteria {

    private final List<Filter> filters;
    private final Order order;
    private final Optional<Integer> limit;
    private final Optional<Integer> page;

    public Criteria(List<Filter> filters, Order order, Optional<Integer> page, Optional<Integer> limit) {
        this.filters = filters;
        this.order = order;
        this.page = page;
        this.limit = limit;
    }

    public Criteria(List<Filter> filters, Order order) {
        this.filters = filters;
        this.order = order;
        this.limit = Optional.empty();
        this.page = Optional.empty();
    }

    public List<Filter> filters() {
        return filters;
    }

    public Order order() {
        return order;
    }

    public Optional<Integer> limit() {
        return limit;
    }

    public Optional<Integer> page() {
        return page;
    }

    public boolean hasFilters() {
        return filters.size() > 0;
    }

    public boolean hasPagination() {
        return page().isPresent() && limit().isPresent();
    }

    public boolean hasOrder() {
        return order().hasOrder();
    }

    public String serialize() {
        return String.format(
                "%s~~%s~~%s~~%s",
                filters.stream().map(Filter::serialize).collect(Collectors.joining("^")),
                order.serialize(),
                page.orElse(0),
                limit.orElse(0)
        );
    }

}
