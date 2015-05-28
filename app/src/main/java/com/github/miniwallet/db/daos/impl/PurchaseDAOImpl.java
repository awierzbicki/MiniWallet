package com.github.miniwallet.db.daos.impl;

import android.util.Log;

import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.entities.PriceTable;
import com.github.miniwallet.db.daos.impl.entities.ProductTable;
import com.github.miniwallet.db.daos.impl.entities.PurchaseTable;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;

import java.util.Date;
import java.util.List;

public class PurchaseDAOImpl implements PurchaseDAO {
    private static final String TAG = "PurchaseDAO";

    @Override
    public List<Purchase> getAllPurchases() {
        Log.d(TAG, "Getting all purchases");
        List<PurchaseTable> purchaseTable = PurchaseTable.listAll(PurchaseTable.class);
        return ListUtils.convertList(purchaseTable);
    }

    @Override
    public List<Purchase> getPurchasesFrom(Date start) {
        Log.d(TAG, "Getting all purchases from " + start);
        return getPurchasesBetween(start, new Date());
    }

    @Override
    public List<Purchase> getPurchasesBetween(Date start, Date end) {
        Log.d(TAG, "Getting all purchases from " + start + " to " + end);

        long startLong = start.getTime();
        long endLong = end.getTime();

        List<PurchaseTable> purchaseTable = PurchaseTable.find(PurchaseTable.class,
                "date between ? and ?", String.valueOf(startLong), String.valueOf(endLong));

        return ListUtils.convertList(purchaseTable);
    }

    @Override
    public double getExpensesFrom(Date start) {
        Log.d(TAG, "Getting expenses from " + start);
        return getExpensesBetween(start, new Date());
    }

    @Override
    public double getExpensesBetween(Date start, Date end) {
        Log.d(TAG, "Getting expenses from " + start + " to " + end);

        long startLong = start.getTime();
        long endLong = end.getTime();

        List<PriceTable> purchases = PriceTable.find(PriceTable.class,
                "date between ? and ?", String.valueOf(startLong), String.valueOf(endLong));

        double total = 0;
        for (PriceTable purchase : purchases) {
            total += purchase.getPrice();
        }
        Log.d(TAG, "Returning " + total);
        return total;
    }

    @Override
    public List<Purchase> getSortedPurchasesBetween(Date start, Date end, String orderBy) {
        Log.d(TAG, "Getting all purchases from " + start + " to " + end + orderBy);

        long startLong = start.getTime();
        long endLong = end.getTime();

        List<PurchaseTable> purchaseTable = PurchaseTable.find(PurchaseTable.class,
                "date between ? and ?", new String[]{String.valueOf(startLong), String.valueOf(endLong)}, null, orderBy, null);

        return ListUtils.convertList(purchaseTable);
    }

    @Override
    public Long insertPurchase(Purchase purchase) {
        Log.d(TAG, "Inserting purchase " + purchase.toString());

        PurchaseTable purchaseTable = PurchaseTable.create(purchase);
        return purchaseTable.getId();
    }

    @Override
    public List<Purchase> getPurchasesByProduct(Product product) {
        Log.d(TAG, "Getting purchases of product " + product);

        ProductTable productTable = ProductTable.create(product);
        List<PurchaseTable> purchases = PurchaseTable.find(PurchaseTable.class, "product = ?", productTable.getId().toString());
        return ListUtils.convertList(purchases);
    }
}
