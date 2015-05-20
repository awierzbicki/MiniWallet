package com.github.miniwallet.db.daos.impl.entities;

import com.github.miniwallet.shopping.Product;
import com.orm.SugarRecord;

import java.util.List;

public class PriceTable extends SugarRecord<PriceTable> {
    ProductTable product;
    double price;
    long date;

    public PriceTable() {
    }

    public PriceTable(ProductTable product, double price, long date) {
        this.product = product;
        this.price = price;
        this.date = date;
    }

    public ProductTable getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }

    public long getDate() { return date; }

    public static PriceTable getTableRepresentation(Product product, double price) {
        List<PriceTable> priceTable = PriceTable.find(PriceTable.class, "product = ? and price = ?",
                product.getId().toString(), String.valueOf(price));

        return priceTable.isEmpty() ? null : priceTable.get(0);
    }
}
