package com.github.miniwallet.filters;

import com.github.miniwallet.shopping.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deviance on 28.05.15.
 */
public class ProductMinPriceFilter implements ProductPriceFilter {
    private Double min;

    public ProductMinPriceFilter() {
        this.min = Double.MIN_VALUE;
    }

    @Override
    public List<Product> filter(List<Product> values) {
        List<Product> newValues = new ArrayList<>();

        for(Product product : values) {
            if(product.getLastPrice() >= min) {
                newValues.add(product);
            }
        }

        return newValues;
    }

    public void setValue(Double min) {
        this.min = min == null ? Double.MIN_VALUE : min;
    }
}
