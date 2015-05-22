package com.github.miniwallet.db.daos.impl.entities;

import com.github.miniwallet.shopping.Product;
import com.orm.SugarRecord;

import java.util.List;

public class PriceTable extends SugarRecord<PriceTable> {
    long product;
    double price;
    long date;

    public PriceTable() {
    }

    public PriceTable(ProductTable product, double price, long date) {
        this.product = product.getId();
        this.price = price;
        this.date = date;
    }

    public ProductTable getProduct() {
        return ProductTable.findById(ProductTable.class, product);
    }

    public double getPrice() {
        return price;
    }

    public long getDate() { return date; }
}
