package com.github.miniwallet.db.daos.impl;

import com.github.miniwallet.db.daos.ProductDAO;
import com.github.miniwallet.db.daos.impl.entities.CategoryTable;
import com.github.miniwallet.db.daos.impl.entities.PriceTable;
import com.github.miniwallet.db.daos.impl.entities.ProductTable;
import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Long insertProduct(Product product) {
        ProductTable productTable = ProductTable.create(product);
        return productTable.getId();

    }

    @Override
    public void insertAll(List<Product> products) {
        for(Product product : products) {
            insertProduct(product);
        }
    }

    @Override
    public void modifyProductPrice(Product product, double newPrice) {
        ProductTable productTable =  ProductTable.getTableRepresentation(product);
        if(productTable == null) {
            throw new RuntimeException("Can't modify product that hasn't been inserted");
        } else {
            productTable.setLastPrice(newPrice);
            product.setLastPrice(newPrice);
            productTable.save();
        }
    }


    @Override
    public Map<Date, Double> getPriceHistoryForProduct(Product product) {
        Map<Date, Double> priceByDate = Maps.newHashMap();
        List<PriceTable> prices = PriceTable.find(PriceTable.class, "product = ?",
                ProductTable.create(product).getId().toString());

        for(PriceTable price : prices) {
            priceByDate.put(new Date(price.getDate()), price.getPrice());
        }
        return priceByDate;
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        CategoryTable categoryTable = CategoryTable.create(category);
        List<ProductTable> products = ProductTable.find(ProductTable.class, "category = ?", categoryTable.getId().toString());
        return ListUtils.convertList(products);
    }

    @Override
    public Product getProductByName(String name) {
        List<ProductTable> product = ProductTable.find(ProductTable.class, "name = ?", name);

        return product.isEmpty() ? null : product.get(0).convert();
    }

    @Override
    public List<Product> getMostBoughtProducts(int count) {
        List<ProductTable> productsOrm = ProductTable.find(ProductTable.class, null, null, null,
                "total_Purchases DESC", String.valueOf(count));

        return ListUtils.convertList(productsOrm);
    }
}
