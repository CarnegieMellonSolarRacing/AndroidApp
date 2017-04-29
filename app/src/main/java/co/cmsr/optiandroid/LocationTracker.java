package co.cmsr.optiandroid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by jonbuckley on 4/29/17.
 */

public class LocationTracker implements LocationListener {
    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; //1000 * 60 * 1; // 1 minute
    private static final int REQUEST_LOCATION_PERMISSIONS_CODE = 2719;
    private static final float MPS_TO_MPH = 2.23694f;

    private static LocationTracker instance;
    private float speed;
    private boolean enabled;
    private boolean gpsEnabled;

    public static LocationTracker getInstance(Activity mainActivity) {
        if (instance == null) {
            instance = new LocationTracker(mainActivity);
        }
        return instance;
    }

    /*
     Private constructor for singleton pattern.
     */
    private LocationTracker(Activity mainActivity) {
        speed = 0.0f;
        enabled = false;

        initializeLocationService(mainActivity);
    }

    private void initializeLocationService(Activity mainActivity) {
        if (ContextCompat.checkSelfPermission(
                mainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        mainActivity,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // If no permissions, request it.
//            ActivityCompat.requestPermissions(
//                    mainActivity,
//                    new String[] {
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION
//                    },
//                    REQUEST_LOCATION_PERMISSIONS_CODE);
            System.out.println("FAILURE -- does not have location permissions");
            return;
        } else {
            enabled = true;
        }
        LocationManager lm = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                MIN_TIME_BW_UPDATES,
                this,
                Looper.getMainLooper());

        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEnabled) {
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    MIN_TIME_BW_UPDATES,
                    this,
                    Looper.getMainLooper());
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public float getSpeed() {
        return speed;
    }

    public float getSpeedMph() {
        return speed * MPS_TO_MPH;
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("got new location");
        if (location.hasSpeed()) {
            speed = location.getSpeed();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
