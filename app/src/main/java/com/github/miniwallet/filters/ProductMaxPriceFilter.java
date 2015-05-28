package com.github.miniwallet.filters;

import com.github.miniwallet.shopping.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deviance on 28.05.15.
 */
public class ProductMaxPriceFilter implements ProductPriceFilter {
    private Double max;

    public ProductMaxPriceFilter() {
        this.max = Double.MAX_VALUE;
    }

    @Override
    public List<Product> filter(List<Product> values) {
        List<Product> newValues = new ArrayList<>();

        for(Product product : values) {
            if(product.getLastPrice() <= max) {
                newValues.add(product);
            }
        }

        return newValues;
    }

    public void setValue(Double max) {
        this.max = max == null ? Double.MAX_VALUE : max;
    }
}
