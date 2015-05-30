package com.github.miniwallet.db.daos.impl;

import android.util.Log;

import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.impl.entities.CategoryTable;
import com.github.miniwallet.db.daos.impl.entities.ProductTable;
import com.github.miniwallet.db.daos.impl.entities.PurchaseTable;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductDAOImpl implements ProductDAO {
    private final static String TAG = "ProductDAO";

    @Override
    public Long insertProduct(Product product) {
        Log.d(TAG, "Inserting " + product.toString());

        ProductTable productTable = ProductTable.create(product);
        return productTable.getId();

    }

    @Override
    public void insertAll(List<Product> products) {
        Log.d(TAG, "Inserting " + products.toString());

        for (Product product : products) {
            insertProduct(product);
        }
    }

    @Override
    public void modifyProductPrice(Product product, double newPrice) {
        Log.d(TAG, "Modyfing products " + product.toString() + " price to " + newPrice);

        ProductTable productTable = ProductTable.getTableRepresentation(product);
        if (productTable == null) {
            throw new RuntimeException("Can't modify product that hasn't been inserted");
        } else {
            productTable.setLastPrice(newPrice);
            product.setLastPrice(newPrice);
            productTable.save();
        }
    }

    @Override
    public void modifyProductCategory(Product product, Category category) {
        Log.d(TAG, "Modyfing products " + product.toString() + " category to " + category.toString());

        ProductTable productTable = ProductTable.getTableRepresentation(product);
        CategoryTable categoryTable = CategoryTable.create(category);
        if (productTable == null) {
            throw new RuntimeException("Can't modify product that hasn't been inserted");
        } else {
            productTable.setCategory(categoryTable);
            product.setCategory(category);
            productTable.save();
        }
    }

    @Override
    public List<Product> getProductsInPriceRange(double min, double max) {
        Log.d(TAG, "products in price range (" + min + " - " + max + ")");

        List<ProductTable> productsOrm = ProductTable.find(ProductTable.class, "last_price between ? and ?",
                String.valueOf(min), String.valueOf(max));
        return ListUtils.convertList(productsOrm);
    }

    @Override
    public List<Product> getProductsByCategoryInPriceRange(Category category, double min, double max) {
        CategoryTable categoryTable = CategoryTable.create(category);
        List<ProductTable> productsOrm = ProductTable.find(ProductTable.class, "category = ? and last_price between ? and ?",
                categoryTable.getId().toString(), String.valueOf(min), String.valueOf(max));
        return ListUtils.convertList(productsOrm);
    }

    @Override
    public Double getHighestPrice() {
        List<ProductTable> productsOrm = ProductTable.find(ProductTable.class, null, null, null,
                "last_price DESC", String.valueOf(1));
        return productsOrm.isEmpty() ? 0 : productsOrm.get(0).getLastPrice();
    }


    @Override
    public Map<Date, Double> getPriceHistoryForProduct(Product product) {
        Log.d(TAG, "price history for product " + product.toString());
        Map<Date, Double> priceByDate = Maps.newHashMap();
        List<PurchaseTable> purchases = PurchaseTable.find(PurchaseTable.class, "product = ?",
                ProductTable.create(product).getId().toString());

        for (PurchaseTable p : purchases) {
            priceByDate.put(p.getDate(), p.getPrice());
        }
        return priceByDate;
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        Log.d(TAG, "get products by category  " + category.toString());
        CategoryTable categoryTable = CategoryTable.create(category);
        List<ProductTable> products = ProductTable.find(ProductTable.class, "category = ?", categoryTable.getId().toString());
        return ListUtils.convertList(products);
    }

    @Override
    public Product getProductByName(String name) {
        Log.d(TAG, "get product " + name);
        List<ProductTable> product = ProductTable.find(ProductTable.class, "name = ?", name);

        return product.isEmpty() ? null : product.get(0).convert();
    }

    @Override
    public List<Product> getMostBoughtProducts(int count) {
        Log.d(TAG, "get " + count + " most bought products");
        List<ProductTable> productsOrm = ProductTable.find(ProductTable.class, null, null, null,
                "total_Purchases DESC", String.valueOf(count));

        return ListUtils.convertList(productsOrm);
    }

    @Override
    public List<Product> getAllProducts() {
        Log.d(TAG, "get all products");
        List<ProductTable> productsOrm = ProductTable.listAll(ProductTable.class);

        return ListUtils.convertList(productsOrm);
    }
}
