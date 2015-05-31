package com.github.miniwallet.adapters;

import android.content.Context;
import android.util.Log;

import com.github.miniwallet.filters.ComplexFilter;
import com.github.miniwallet.shopping.experimental.ViewHolder;

import java.util.List;

/**
 * Created by deviance on 28.05.15.
 */
public class FilterableEntityListAdapter<T> extends EntityListAdapter<T> {
    private final ComplexFilter<T> filter;

    public FilterableEntityListAdapter(Context context, List<T> values, ViewHolder.Type type, ComplexFilter<T> filter) {
        super(context, values, type);
        this.filter = filter;
    }

    @Override
    public T getItem(int position) {
        return values.get(position);
    }

    @Override
    public void setNewValuesAndNotify(List<T> newValues) {
        filter.setValues(newValues);
        super.values = filter.performFiltering();
        notifyDataSetChanged();
    }

    public void filterAndNotify() {
        super.values = filter.performFiltering();
        notifyDataSetChanged();
    }
}
