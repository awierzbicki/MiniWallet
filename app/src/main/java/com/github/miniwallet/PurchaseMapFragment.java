package com.github.miniwallet;

import android.os.Bundle;

import com.github.miniwallet.db.daos.PurchaseDAO;
import com.github.miniwallet.db.daos.impl.PurchaseDAOImpl;
import com.github.miniwallet.maps.PurchaseRenderer;
import com.github.miniwallet.shopping.Purchase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Random;

public class PurchaseMapFragment extends SupportMapFragment {
    private static final Random random = new Random();
    private static final double LAT = 51.107885;
    private static final double LNG = 17.038538;

    private GoogleMap map;
    private PurchaseDAO purchaseDAO;
    private ClusterManager<Purchase> clusterManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        purchaseDAO = new PurchaseDAOImpl();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) return;

        map = getMap();
        start();
    }

    protected void start() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.107885, 17.038538), 9.5f));
        clusterManager = new ClusterManager<>(getActivity(), getMap());
        clusterManager.setRenderer(new PurchaseRenderer(getActivity(), map, clusterManager));
        clusterManager.addItems(purchaseDAO.getAllPurchases());
        clusterManager.cluster();

        map.setOnCameraChangeListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        map.setOnInfoWindowClickListener(clusterManager);
    }
}
