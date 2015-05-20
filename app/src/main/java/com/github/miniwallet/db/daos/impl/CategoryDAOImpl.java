package com.github.miniwallet.db.daos.impl;

import com.github.miniwallet.db.daos.CategoryDAO;
import com.github.miniwallet.db.daos.impl.entities.CategoryTable;
import com.github.miniwallet.shopping.Category;

import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public List<Category> getAllCategories() {
        List<CategoryTable> categoriesOrm = CategoryTable.listAll(CategoryTable.class);
        return ListUtils.convertList(categoriesOrm);
    }

    @Override
    public Long insertCategory(Category category) {
        CategoryTable categoryTable = CategoryTable.create(category);
        return categoryTable.getId();
    }
}
