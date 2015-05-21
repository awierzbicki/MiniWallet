package com.github.miniwallet.db.daos.impl.entities;

import com.github.miniwallet.shopping.Product;
import com.orm.SugarRecord;

import java.util.List;

public class ProductTable extends SugarRecord<ProductTable> implements AbstractTable<Product> {
    double lastPrice;
    int totalPurchases;
    String name;
    CategoryTable category;

    public ProductTable() {
    }

    public Product convert() {
        return new Product(getId(), category.convert(), lastPrice, name, totalPurchases);
    }

    public static ProductTable create(Product product) {
        ProductTable productTable = getTableRepresentation(product);
        if(productTable == null) {
            productTable = new ProductTable(product);
            productTable.save();
        }
        product.setId(productTable.getId());
        return productTable;
    }

    public ProductTable(Product product) {
        this.name = product.getName();
        this.totalPurchases = product.getTotalPurchases();
        this.lastPrice = product.getLastPrice();
        this.category = CategoryTable.create(product.getCategory());
    }

    public static ProductTable getTableRepresentation(Product product) {
        if(product.getId() == null) {
            List<ProductTable> lookup = ProductTable.find(ProductTable.class, "name = ?", product.getName());
            return lookup.isEmpty() ? null : lookup.get(0);
        } else {
            return ProductTable.findById(ProductTable.class, product.getId());
        }
    }

    public void incrementTotalPurchases() { totalPurchases += 1; }

    public double getLastPrice() {
        return lastPrice;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public String getName() {
        return name;
    }

    public CategoryTable getCategory() {
        return category;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    @Override
    public String toString() {
        return "ProductTable{" +
                "lastPrice=" + lastPrice +
                ", totalPurchases=" + totalPurchases +
                ", name='" + name + '\'' +
                ", category=" + category +
                '}';
    }

    public void setCategory(CategoryTable category) {
        this.category = category;
    }
}
