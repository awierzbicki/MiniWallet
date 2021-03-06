package com.github.miniwallet.db.daos;

import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public interface PurchaseDAO {
    public List<Purchase> getAllPurchases();

    public List<Purchase> getPurchasesFrom(Date start);

    public List<Purchase> getPurchasesBetween(Date start, Date end);

    public Long insertPurchase(Purchase purchase);

    public List<Purchase> getPurchasesByProduct(Product product);

    public double getExpensesFrom(Date start);

    public double getExpensesBetween(Date start, Date end);

    public List<Purchase> getSortedPurchasesBetween(Date start, Date end, String orderBy, int limit, long skip);

    public LatLng getPurchaseLatLang(long puchaseId);

    public long getPurchasesTotalNumber(Date start, Date end);

}
