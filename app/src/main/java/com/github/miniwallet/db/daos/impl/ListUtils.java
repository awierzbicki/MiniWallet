package com.github.miniwallet.db.daos.impl;

import android.util.Log;

import com.github.miniwallet.db.daos.impl.entities.AbstractTable;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {
    private static final String TAG = "ListUtils";

    private ListUtils() {

    }

    public static <T> List<T> convertList(List<? extends AbstractTable<T>> productsOrm) {
        List<T> products = new ArrayList<>();
        if (productsOrm != null) {
            Log.d(TAG, "Converting list " + productsOrm.toString());
            for (AbstractTable<T> prod : productsOrm) {
                T t = prod.convert();
                Log.d(TAG, "Putting " + t.toString());
                products.add(t);
            }
        }
        Log.d(TAG, "Finished converting");
        return products;
    }
}
