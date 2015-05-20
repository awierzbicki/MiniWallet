package com.github.miniwallet.db.daos.impl.entities;

import com.github.miniwallet.shopping.Category;
import com.orm.SugarRecord;

import java.util.List;

public class CategoryTable extends SugarRecord<CategoryTable> implements AbstractTable<Category> {
    String name;

    public CategoryTable() {
    }

    public CategoryTable(String name) {
        this.name = name;
    }

    public static CategoryTable create(Category category) {
        CategoryTable categoryTable = getTableRepresentation(category);
        if(categoryTable == null) {
            categoryTable = new CategoryTable(category.getName());
            categoryTable.save();
        }
        category.setId(categoryTable.getId());
        return categoryTable;
    }

    private static String idOrNull(Category category) {
        return category.getId() == null ? null : category.getId().toString();
    }

    public Category convert() {
        return new Category(getId(), name);
    }

    public String getName() {
        return name;
    }

    public static CategoryTable getTableRepresentation(Category category) {
        if(category.getId() == null) {
            List<CategoryTable> lookup = CategoryTable.find(CategoryTable.class, "name = ?",
                    category.getName());

            return lookup.isEmpty() ? null : lookup.get(0);
        } else {
            return CategoryTable.findById(CategoryTable.class, category.getId());
        }
    }

    @Override
    public String toString() {
        return "CategoryTable{" +
                "name='" + name + '\'' +
                '}';
    }
}

