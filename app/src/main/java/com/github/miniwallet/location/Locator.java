package com.github.miniwallet.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by deviance on 31.05.15.
 */
public class Locator implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "LOCATOR";
    private final GoogleApiClient api;
    private Location lastLocation;
    private Activity activity;

    public Locator(Activity activity) {
        this.activity = activity;
        api = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(api);
    }

    public void connect() {
        api.connect();
    }

    public void disconnect() {
        api.disconnect();
    }


    public LatLng getLocation() {
        Log.d(TAG, "location requested!");
        if (lastLocation != null) {
            Log.d(TAG, "returning long lat " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
            return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        } else {
            LocationManager locManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            for (String provider : locManager.getAllProviders()) {
                Location location = locManager.getLastKnownLocation(provider);
                if (location != null) {
                    Log.d(TAG, "returning long lat " + location.getLatitude() + ", " + location.getLongitude());
                    return new LatLng(location.getLatitude(), location.getLongitude());
                }
            }
        }

        Log.d(TAG, "returning long lat 1, 1");
        return new LatLng(1, 1);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(api);

        LocationRequest req = new LocationRequest();
        req.setInterval(100000);
        req.setFastestInterval(50000);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(api, req, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location updated to " + location.getLatitude() + ", " + location.getLongitude());
        lastLocation = location;
    }
}
