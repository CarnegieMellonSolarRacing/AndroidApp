package co.cmsr.optiandroid.datastructures;

import android.app.Activity;
import android.content.Context;

import co.cmsr.optiandroid.LocationTracker;

/**
 * Created by jonbu on 4/30/2017.
 */

public class LocalDataGenerator {
    LocationTracker locationTracker;

    public LocalDataGenerator(Context context) {
        locationTracker = LocationTracker.getInstance((Activity) context);
    }

    public LocalDataPacket gatherLocalData() {
        double speed = -1.0f;
        if (locationTracker.isEnabled()) {
            speed = locationTracker.getSpeedMph();
        }

        return new LocalDataPacket(speed);
    }
}
