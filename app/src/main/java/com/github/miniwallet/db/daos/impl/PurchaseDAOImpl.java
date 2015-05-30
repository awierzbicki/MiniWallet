package com.github.miniwallet.db.daos.impl;

import android.util.Log;

import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.entities.PriceTable;
import com.github.miniwallet.db.daos.impl.entities.ProductTable;
import com.github.miniwallet.db.daos.impl.entities.PurchaseTable;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;

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
    public List<Purchase> getSortedPurchasesBetween(Date start, Date end, String orderBy, int limit, long skip) {
        Log.d(TAG, "Getting all purchases from " + start + " to " + end + orderBy);

        long startLong = start.getTime();
        long endLong = end.getTime();


        //List<PurchaseTable> purchaseTable = PurchaseTable.findWithQuery(PurchaseTable.class, "SELECT * FROM purchase_table ORDER BY price DESC LIMIT 50");
        List<PurchaseTable> purchaseTable = PurchaseTable.findWithQuery(PurchaseTable.class, "SELECT * FROM purchase_table WHERE date between ? and ? ORDER BY ? LIMIT ? OFFSET ?",
                String.valueOf(startLong), String.valueOf(endLong), orderBy, String.valueOf(limit), String.valueOf(skip));
//        List<PurchaseTable> purchaseTable = PurchaseTable.find(PurchaseTable.class,
//                "date between ? and ?", new String[]{String.valueOf(startLong), String.valueOf(endLong)}, null, orderBy, null);
        List<Purchase> result = ListUtils.convertList(purchaseTable);
        Log.i(TAG, "Fisrt=" + result.get(0));
        Log.i(TAG, "Last=" + result.get(result.size() - 1));
        return result;
    }

    @Override
    public LatLng getPurchaseLatLang(long purchaseId) {
        List<PurchaseTable> purchaseTable = PurchaseTable.find(PurchaseTable.class,
                "id = ?", String.valueOf(purchaseId));
        return purchaseTable.isEmpty() ? null : purchaseTable.get(0).getLocation();
    }

    @Override
    public long getPurchasesTotalNumber(Date start, Date end) {
        long startLong = start.getTime();
        long endLong = end.getTime();
        return PurchaseTable.count(PurchaseTable.class, "date between ? and ?", new String[]{String.valueOf(startLong), String.valueOf(endLong)});
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
