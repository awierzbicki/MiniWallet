package com.github.miniwallet.db.daos.impl.entities;

import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

import java.util.Date;

public class PurchaseTable extends SugarRecord<PurchaseTable> implements AbstractTable<Purchase> {
    PriceTable price;
    ProductTable product;
    double lat;
    double lng;
    long date;

    public PurchaseTable(PriceTable price, ProductTable product, LatLng location, Date date) {
        this.price = price;
        this.product = product;
        this.lat = location.latitude;
        this.lng = location.longitude;
        this.date = date.getTime();
    }

    public PurchaseTable() {
    }

    public PurchaseTable(Purchase purchase) {
        ProductTable productTable = extractProductTable(purchase);
        PriceTable priceTable = extractPriceTable(purchase, productTable);

        this.product = productTable;
        this.price = priceTable;
        this.date = purchase.getDate().getTime();
        this.lat = purchase.getLocation().latitude;
        this.lng = purchase.getLocation().longitude;
    }

    private ProductTable extractProductTable(Purchase purchase) {
        ProductTable productTable = ProductTable.create(purchase.getProduct());
        productTable.incrementTotalPurchases();
        productTable.save();

        purchase.getProduct().setTotalPurchases(productTable.getTotalPurchases());

        return productTable;
    }

    private PriceTable extractPriceTable(Purchase purchase, ProductTable productTable) {
        PriceTable priceTable = PriceTable.getTableRepresentation(purchase.getProduct(), purchase.getPrice());
        if (priceTable == null) {
            priceTable = new PriceTable(productTable, purchase.getPrice(), purchase.getDate().getTime());
            priceTable.save();
        }
        return priceTable;
    }

    public Purchase convert() {
        return new Purchase(getId(), price.getPrice(), product.convert(), new LatLng(lat, lng),
                new Date(date));
    }

    public PriceTable getPrice() {
        return price;
    }

    public ProductTable getProduct() {
        return product;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lng);
    }

    public Date getDate() {
        return new Date(date);
    }

    @Override
    public String toString() {
        return "PurchaseTable{" +
                "price=" + price +
                ", product=" + product +
                ", location=" + new LatLng(lat, lng) +
                ", date=" + date +
                '}';
    }

    public static PurchaseTable getTableRepresentation(Purchase purchase) {
        if (purchase.getId() == null) return null;
        return PurchaseTable.findById(PurchaseTable.class, purchase.getId());
    }

    public static PurchaseTable create(Purchase purchase) {
        PurchaseTable purchaseTable = getTableRepresentation(purchase);
        if (purchaseTable == null) {
            purchaseTable = new PurchaseTable(purchase);
            purchaseTable.save();
        }
        purchase.setId(purchaseTable.getId());
        return purchaseTable;
    }
}
