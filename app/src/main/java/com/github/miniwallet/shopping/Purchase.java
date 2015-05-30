package com.github.miniwallet.shopping;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Date;

public class Purchase implements ClusterItem {
    private Long id;
    private double price;
    private Product product;
    private LatLng location;
    private Date date;

    public Purchase(double price, Product product, LatLng location, Date date) {
        this.price = price;
        this.product = product;
        this.location = location;
        this.date = date;
    }

    public Purchase(Long id, double price, Product product, LatLng location, Date date) {
        this.id = id;
        this.price = price;
        this.product = product;
        this.location = location;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public LatLng getPosition() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "price=" + price +
                ", location=" + location +
                ", product=" + product +
                ", date=" + date +
                '}';
    }
}
