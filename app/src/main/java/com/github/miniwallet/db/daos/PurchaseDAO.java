package com.github.miniwallet.db.daos;

import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;

import java.util.Date;
import java.util.List;

public interface PurchaseDAO {
    public List<Purchase> getAllPurchases();
    public List<Purchase> getPurchasesFrom(Date start);
    public List<Purchase> getPurchasesBetween(Date start, Date end);
    public Long insertPurchase(Purchase purchase);
    public List<Purchase> getPurchasesByProduct(Product product);
}
