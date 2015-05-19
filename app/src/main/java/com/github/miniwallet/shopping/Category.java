package com.github.miniwallet.shopping;

import com.github.miniwallet.db.daos.impl.entities.*;
/*
 * Created by deviance on 19.05.15.
 */
public class Category {
    private Long id;
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}
