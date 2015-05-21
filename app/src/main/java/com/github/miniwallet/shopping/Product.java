package com.github.miniwallet.shopping;

public class Product {
    private Long id;
    private Category category;
    private double lastPrice;
    private String name;
    private int totalPurchases;

    public Product(Category category, double lastPrice, String name, int totalPurchases) {
        this.category = category;
        this.lastPrice = lastPrice;
        this.name = name;
        this.totalPurchases = totalPurchases;
    }

    public Product(Long id, Category category, double lastPrice, String name, int totalPurchases) {
        this.id = id;
        this.category = category;
        this.lastPrice = lastPrice;
        this.name = name;
        this.totalPurchases = totalPurchases;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public String getName() {
        return name;
    }

    public int getTotalPurchases() {
        return totalPurchases;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public void setTotalPurchases(int totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    @Override
    public String toString() {
        return "Product{" +
                "category=" + category.toString() +
                ", lastPrice=" + lastPrice +
                ", name='" + name + '\'' +
                ", totalPurchases=" + totalPurchases +
                '}';
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
