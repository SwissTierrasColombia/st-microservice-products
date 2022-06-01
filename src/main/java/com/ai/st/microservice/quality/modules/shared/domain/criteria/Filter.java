package com.ai.st.microservice.quality.modules.shared.domain.criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class Filter {

    private final FilterField field;
    private final FilterOperator operator;
    private final FilterValue value;
    private final List<FilterValue> values;

    public Filter(FilterField field, FilterOperator operator, FilterValue value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.values = new ArrayList<>();
    }

    public Filter(FilterField field, FilterOperator operator, List<FilterValue> values) {
        this.field = field;
        this.operator = operator;
        this.values = values;
        this.value = null;
    }

    public static Filter create(String field, String operator, String value) {
        return new Filter(new FilterField(field), FilterOperator.fromValue(operator.toUpperCase()),
                new FilterValue(value));
    }

    public static Filter create(String field, String operator, List<String> values) {
        return new Filter(new FilterField(field), FilterOperator.fromValue(operator.toUpperCase()),
                values.stream().map(FilterValue::new).collect(Collectors.toList()));
    }

    public static Filter fromValues(HashMap<String, String> values) {
        return new Filter(new FilterField(values.get("field")), FilterOperator.fromValue(values.get("operator")),
                new FilterValue(values.get("value")));
    }

    public FilterField field() {
        return field;
    }

    public FilterOperator operator() {
        return operator;
    }

    public FilterValue value() {
        return value;
    }

    public List<FilterValue> values() {
        return values;
    }

    public String serialize() {
        return String.format("%s.%s.%s", field.value(), operator.value(), value.value());
    }

}
