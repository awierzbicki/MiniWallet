package com.github.miniwallet.db.daos.impl;

import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.entities.PurchaseTable;
import com.github.miniwallet.shopping.Purchase;

import java.util.Date;
import java.util.List;

public class PurchaseDAOImpl implements PurchaseDAO {
    @Override
    public List<Purchase> getAllPurchases() {
        List<PurchaseTable> purchaseTable = PurchaseTable.listAll(PurchaseTable.class);
        return ListUtils.convertList(purchaseTable);
    }

    @Override
    public List<Purchase> getPurchasesFrom(Date start) {
        return getPurchasesBetween(start, new Date());
    }

    @Override
    public List<Purchase> getPurchasesBetween(Date start, Date end) {
        long startLong = start.getTime();
        long endLong = end.getTime();

        List<PurchaseTable> purchaseTable = PurchaseTable.find(PurchaseTable.class,
                "date between ? and ?", String.valueOf(startLong), String.valueOf(endLong));

        return ListUtils.convertList(purchaseTable);
    }

    @Override
    public Long insertPurchase(Purchase purchase) {
        PurchaseTable purchaseTable = PurchaseTable.create(purchase);
        purchase.setId(purchaseTable.getId());
        return purchaseTable.getId();
    }
}
