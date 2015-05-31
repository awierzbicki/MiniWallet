package com.github.miniwallet.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by deviance on 31.05.15.
 */
public class Locator {
    private Activity activity;

    public Locator(Activity activity) {
        this.activity = activity;
    }

    public LatLng getLocation() {
        LocationManager locManager =(LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location location = locManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = 1;
        double latitude = 1;
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("LOCATOR", "returning long lat " + longitude + ", " + latitude);
        }
        return new LatLng(latitude, longitude);
    }
}
