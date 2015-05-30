package com.github.miniwallet;

import android.os.Bundle;

import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.maps.PurchaseRenderer;
import com.github.miniwallet.shopping.Product;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PurchaseMapFragment extends SupportMapFragment {
    private static final Random random = new Random();
    private static final double LAT = 51.107885;
    private static final double LNG = 17.038538;

    private GoogleMap map;
    private PurchaseDAO purchaseDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purchaseDAO = new PurchaseDAOImpl();
    }

    @Override
    public void onResume() {
        super.onResume();
        map = getMap();
        start();
    }

    protected void start() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.107885, 17.038538), 9.5f));
        ClusterManager<Purchase> clusterManager = new ClusterManager<>(getActivity(), getMap());
        clusterManager.setRenderer(new PurchaseRenderer(getActivity(), map, clusterManager));
        clusterManager.addItems(randomPurchases(30));
        clusterManager.cluster();

        map.setOnCameraChangeListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        map.setOnInfoWindowClickListener(clusterManager);
    }

    private List<Product> randomProducts(int n) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = RandomStringUtils.randomAlphabetic(6).toLowerCase();
            products.add(new Product(null, random.nextInt(20), name, random.nextInt(100)));
        }
        return products;
    }

    private List<Purchase> randomPurchases(int n) {
        List<Purchase> purchases = new ArrayList<>();
        for (Product p : randomProducts(n)) {
            purchases.add(new Purchase(random.nextInt(100), p, position(), new Date()));
        }
        return purchases;
    }

    private LatLng position() {
        return new LatLng(random(LAT + 0.1691572, LAT - 0.1182459), random(LNG + 0.274717, LNG - 0.2250223));
    }

    private double random(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }
}
