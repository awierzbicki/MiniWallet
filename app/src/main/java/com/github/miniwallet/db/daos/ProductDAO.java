package com.github.miniwallet.db.daos;

import com.github.miniwallet.shopping.Product;

import java.util.List;

/**
 * Created by deviance on 18.05.15.
 */
public interface ProductDAO {
    public List<Product> getMostBoughtProducts(int count);
    public Long insertProduct(Product product);
    public Product getProductByName(String name);
}
