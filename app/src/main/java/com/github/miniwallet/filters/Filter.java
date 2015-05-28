package com.github.miniwallet.filters;

import java.util.List;


public interface Filter<T> {
    List<T> filter(List<T> values);
}
