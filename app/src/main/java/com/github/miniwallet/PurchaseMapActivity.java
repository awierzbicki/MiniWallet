package com.github.miniwallet;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Agnieszka on 2015-05-28.
 */
public class PurchaseMapActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private MapFragment mapFragment;
    private LatLng purchaseLatLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate");
        setContentView(R.layout.activity_maps);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        double lat = getIntent().getDoubleExtra("PurchaseLat", 0);
        double lng = getIntent().getDoubleExtra("PurchaseLng", 0);
        purchaseLatLang = new LatLng(lat, lng);
    }

    @Override
    public void onMapLoaded() {
        mapFragment.getMap().addMarker(new MarkerOptions().position(purchaseLatLang));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
