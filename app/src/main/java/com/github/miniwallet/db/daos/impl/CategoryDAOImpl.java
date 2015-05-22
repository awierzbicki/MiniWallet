package com.github.miniwallet.db.daos.impl;

import android.util.Log;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.impl.entities.CategoryTable;
import com.github.miniwallet.shopping.Category;

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
}
