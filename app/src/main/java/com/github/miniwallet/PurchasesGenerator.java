package com.github.miniwallet;

import com.github.miniwallet.shopping.Category;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Dominik on 2015-05-30.
 */
public class PurchasesGenerator {
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
    private static final Product[] products = {
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

    private static final LatLng places[] = {
            new LatLng(51.112372, 17.059421), // Pasaz Grunwaldzki
            new LatLng(51.108199, 17.040144), // Galeria Dominikanska
            new LatLng(51.110206, 17.032097), // Rynek
            new LatLng(51.119508, 16.990010), // Magnolia
    };

    private static ArrayList<Date> getMonthsDates() {
        ArrayList<Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for(int i = 0; i < 12; i++) {
            cal.set(Calendar.MONTH, i);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            dates.add(cal.getTime());
        }
        cal.set(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.YEAR, 1);
        dates.add(cal.getTime());
        return dates;
    }

    public static  ArrayList<Purchase> fabricatePurchases(int maxProductsPerMonth) {
        ArrayList<Purchase> result = new ArrayList<>();
        Random r = new Random();
        Date today = new Date();
        Product product;
        LatLng latLng;
        for(Date date : getMonthsDates()) {
            if(today.getTime() > date.getTime()) {
                int n = r.nextInt(maxProductsPerMonth)+1;
                for (int i = 0; i < n; i++) {
                    int pro = r.nextInt(products.length);
                    int lat = r.nextInt(places.length);
                    double priceChanger = r.nextDouble()+0.5;
                    product = products[pro];
                    latLng = places[lat];
                    latLng = new LatLng(latLng.longitude + r.nextDouble() * 0.001, latLng.latitude + r.nextDouble() * 0.001);
                    date.setTime(date.getTime() + 1000*3600);
                    result.add(new Purchase(product.getLastPrice()*priceChanger, product, latLng, date));
                }
            }
        }
        return result;
    }
}
