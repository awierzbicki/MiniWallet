package com.github.miniwallet.db.daos;

import com.github.miniwallet.shopping.Category;

import java.util.List;

/**
 * Created by deviance on 18.05.15.
 */
public interface CategoryDAO {
    public List<Category> getAllCategories();
    public Long insertCategory(Category category);
}
