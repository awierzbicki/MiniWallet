package com.github.miniwallet.db.daos.impl;

import android.util.Log;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.impl.entities.CategoryTable;
import com.github.miniwallet.shopping.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private static final String TAG = "CategoryDAO";

    @Override
    public List<Category> getAllCategories() {
        Log.d(TAG, "Getting all categories");
        List<CategoryTable> categoriesOrm = CategoryTable.listAll(CategoryTable.class);
        return ListUtils.convertList(categoriesOrm);
    }

    @Override
    public Long insertCategory(Category category) {
        Log.d(TAG, "Inserting category " + category.toString());
        CategoryTable categoryTable = CategoryTable.create(category);
        return categoryTable.getId();
    }

    @Override
    public ArrayList<String> getAllCategoriesNames() {
        Log.d(TAG, "Getting all categories names");
        List<CategoryTable> categoriesOrm = CategoryTable.listAll(CategoryTable.class);
        ArrayList<String> categoriesNames = new ArrayList<>();
        if (categoriesOrm != null) {
            for (CategoryTable c : categoriesOrm) {
                categoriesNames.add(c.getName());
            }
        }
        return categoriesNames;
    }

    public Category getCategoryByName(String name) {
        Log.d(TAG, "get product " + name);
        List<CategoryTable> category = CategoryTable.find(CategoryTable.class, "name = ?", name);

        return category.isEmpty() ? null : category.get(0).convert();
    }
}
