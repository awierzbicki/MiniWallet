package com.github.miniwallet.filters;

import com.github.miniwallet.shopping.Product;

public interface ProductPriceFilter extends Filter<Product> {
    void setValue(Double val);
}
