package com.github.miniwallet.filters;

import com.github.miniwallet.shopping.Product;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ProductNameFilter implements Filter<Product> {
    private String pattern;

    public ProductNameFilter() {
        this.pattern = "";
    }

    @Override
    public List<Product> filter(List<Product> values) {
        if(pattern == null || pattern.isEmpty()) return values;

        List<Product> newValues = new ArrayList<>();
        for(Product product : values) {
            if(StringUtils.containsIgnoreCase(product.getName(), pattern)) {
                newValues.add(product);
            }
        }

        return newValues;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
