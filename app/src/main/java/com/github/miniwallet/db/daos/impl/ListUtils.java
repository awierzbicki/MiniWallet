package com.github.miniwallet.db.daos.impl;

import com.github.miniwallet.db.daos.impl.entities.AbstractTable;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {
    private ListUtils() {

    }

    public static <T> List<T> convertList(List<? extends AbstractTable<T>> productsOrm) {
        List<T> products = new ArrayList<>();
        if (productsOrm != null) {
            for (AbstractTable<T> prod : productsOrm) {
                products.add(prod.convert());
            }
        }
        return products;
    }
}
