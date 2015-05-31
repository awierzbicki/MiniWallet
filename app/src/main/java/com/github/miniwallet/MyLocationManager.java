package com.github.miniwallet;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Agnieszka on 2015-05-31.
 */
public class MyLocationManager {
    LocationManager locationManager;

    public MyLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public Location getBestLocation() {
        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Log.i("MyLocationManager", "GPS=" + gpsLocation + " Net=" + networkLocation);
        long gpsLocationTime = 0;
        long networkLocationTime = 0;
        if (null != gpsLocation) {
            gpsLocationTime = gpsLocation.getTime();
        }
        if (null != networkLocation) {
            networkLocationTime = networkLocation.getTime();
        }
        if (gpsLocationTime > networkLocationTime)
            return gpsLocation;
        else
            return networkLocation;
    }
}
