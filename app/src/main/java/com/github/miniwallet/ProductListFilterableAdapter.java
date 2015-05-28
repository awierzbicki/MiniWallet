package com.github.miniwallet;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import com.github.miniwallet.EntityListAdapter;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.experimental.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agnieszka on 2015-05-23.
 */
public class ProductListFilterableAdapter extends EntityListAdapter<Product> implements Filterable {
    private Filter productFilter;

    public ProductListFilterableAdapter(Context context, List<Product> values) {
        super(context, values, ViewHolder.Type.PURCHASE_ROW);
    }

    private class ProductFilter extends Filter {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            Filter.FilterResults results = new Filter.FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = values;
                results.count = values.size();
            } else {
                List<Product> newValues = new ArrayList<Product>();
                for (Product p : values) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        newValues.add(p);
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setNewValuesAndNotify((List<Product>) results.values);
        }
    }

    @Override
    public Filter getFilter() {
        if (productFilter == null) {
            productFilter = new ProductFilter();
        }
        return productFilter;
    }
}
