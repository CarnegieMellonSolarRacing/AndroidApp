package co.cmsr.optiandroid.datastructures;

import android.app.Activity;
import android.content.Context;

import co.cmsr.optiandroid.LocationTracker;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatData {
    public double solarPanelPower;

    public double batteryCharge;
    public double batteryChargePercent;
    public double batteryTemp;

    public double boatSpeed;

    public static BoatData generateBoatData(
            Context context,
            BoatMap boatMap,
            boolean currentsCalibrated,
            DataPacket dp,
            LocalDataPacket ldp) {
        BoatData bd = new BoatData();

        return bd;
    }
}
