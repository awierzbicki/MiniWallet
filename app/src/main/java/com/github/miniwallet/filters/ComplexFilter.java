package com.github.miniwallet.filters;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ComplexFilter<T> {
    private List<Filter<T>> filters;
    private List<T> values;

    public ComplexFilter(List<T> values, List<Filter<T>> filters) {
        this.filters = filters;
        this.values = values;
    }

    public ComplexFilter(List<T> values, Filter<T>... filters) {
        this.filters = Lists.newArrayList(filters);
        this.values = values;
    }

    public List<T> performFiltering() {
        List<T> newValues = new ArrayList<>(values);
        for(Filter<T> filter : filters) {
            newValues = filter.filter(newValues);
        }
        return newValues;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
}
