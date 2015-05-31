package com.github.miniwallet;

import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.math3.util.Precision;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Dominik on 2015-05-30.
 */
public class PurchasesGenerator {
    private static Random random = new Random(100);
    private static double LAT = 51.112372;
    private static double LNG = 17.059421;
    private static long BEGIN = Timestamp.valueOf("2015-01-01 00:00:00").getTime();
    private static long END = Timestamp.valueOf("2015-05-31 00:58:00").getTime();

    private static final Category[] categories = {
            new Category("Drinks"), // 0
            new Category("Snacks"), // 1
            new Category("Food"), // 2
            new Category("Alcohol"), // 3
            new Category("Groceries"), // 4
            new Category("Tickets"), // 5
            new Category("Newspapers"), // 6
            new Category("Entertainment") // 7
    };

    private static Product[] products = {
            new Product(categories[0], 3.0, "Coffee", 0),
            new Product(categories[0], 1.5, "Pepsi", 0),
            new Product(categories[0], 2.0, "Water", 0),
            new Product(categories[0], 2.5, "Juice", 0),
            new Product(categories[0], 1.5, "Pepsi", 0),
            new Product(categories[0], 1.5, "Pepsi", 0),
            new Product(categories[1], 1.5, "KitKat", 0),
            new Product(categories[1], 3.5, "Lay's", 0),
            new Product(categories[1], 3.1, "Mini Pizza", 0),
            new Product(categories[1], 2.4, "Apple Pie", 0),
            new Product(categories[2], 22.0, "Pizza", 0),
            new Product(categories[2], 11.9, "Mega Pocket", 0),
            new Product(categories[2], 6.0, "Fries", 0),
            new Product(categories[2], 15.0, "Lunch", 0),
            new Product(categories[3], 5.5, "Beer", 0),
            new Product(categories[5], 1.5, "MPK Ticket", 0),
            new Product(categories[6], 1.0, "Fakt", 0),
            new Product(categories[7], 17.5, "Cinema", 0),
            new Product(categories[7], 10.0, "Billard", 0),
            new Product(categories[7], 6.8, "Museum", 0),
    };

    public static Date nextDate() {
        long diff = END - BEGIN + 1;
        return new Date(BEGIN + (long) (Math.random() * diff));
    }

    public static List<Purchase> randomPurchases(int n) {
        List<Purchase> purchases = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            Product p = products[random.nextInt(products.length)];
            double price = random(p.getLastPrice() - 0.5, p.getLastPrice() + 0.5);
            purchases.add(new Purchase(price, p, position(), nextDate()));
        }
        return purchases;
    }

    private static LatLng position() {
        return new LatLng(random(LAT + 0.1691572, LAT - 0.1182459), random(LNG + 0.274717, LNG - 0.2250223));
    }

    private static double random(double min, double max) {
       return Precision.round((random.nextDouble() * (max - min) + min), 2);
    }
}
