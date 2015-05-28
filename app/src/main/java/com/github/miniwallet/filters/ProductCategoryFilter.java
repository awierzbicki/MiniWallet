package com.github.miniwallet.filters;

import android.util.Log;

import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deviance on 28.05.15.
 */
public class ProductCategoryFilter implements Filter<Product> {
    private List<Category> categories;

    public ProductCategoryFilter() {
        categories = new ArrayList<>();
    }

    @Override
    public List<Product> filter(List<Product> values) {
        if (categories == null || categories.isEmpty() || containsAllCategory()) return values;

        List<Product> newValues = new ArrayList<>();
        for (Product product : values) {
            if (categories.contains(product.getCategory())) {
                newValues.add(product);
            }
        }

        return newValues;
    }

    // all category does not exist in DB and so when searched for it it returns null.
    private boolean containsAllCategory() {
        System.out.println("Categories - " + categories);
        for (Category c : categories) {
            if (c == null || c.getName().equalsIgnoreCase("all"))
                return true;
        }
        return false;
    }

    public void setCategories(Category... newCategories) {
        this.categories = Lists.newArrayList(newCategories);
    }

    public void setCategories(List<Category> newCategories) {
        this.categories = newCategories;
    }
}
