package com.github.miniwallet.db.daos;

import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProductDAO {
    public List<Product> getMostBoughtProducts(int count);
    public Long insertProduct(Product product);
    public Product getProductByName(String name);
    public void insertAll(List<Product> products);
    public void modifyProductPrice(Product product, double newPrice);
    public Map<Date, Double> getPriceHistoryForProduct(Product product);
    public List<Product> getProductsByCategory(Category category);
}
