package co.cmsr.optiandroid.datastructures;

import android.app.Activity;
import android.content.Context;

import co.cmsr.optiandroid.LocationTracker;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatData {
    public boolean currentsCalibrated;

    public double solarPanelCurrent;
    public double solarPanelVoltage;

    public double chargeControllerCurrent;

    public double batteryAVoltage;
    public double batteryBVoltage;

    public double motorCurrent;

    public double speed;

    public static BoatData generateBoatData(
            Context context,
            BoatMap boatMap,
            boolean currentsCalibrated,
            DataPacket dp,
            LocalDataPacket ldp) {
        BoatData bd = new BoatData();

        bd.currentsCalibrated = currentsCalibrated;

        int numCurrents = dp.currents.size();
        int numVoltages = dp.voltages.size();

        if (numCurrents > boatMap.solarPanelVoltageIndex) {
            bd.solarPanelCurrent = dp.currents.get(boatMap.solarPanelVoltageIndex);
        }

        if (numVoltages > boatMap.solarPanelVoltageIndex) {
            bd.solarPanelVoltage = dp.voltages.get(boatMap.solarPanelVoltageIndex);
        }

        if (numCurrents > boatMap.chargeControllerCurrentIndex) {
            bd.chargeControllerCurrent = dp.currents.get(boatMap.chargeControllerCurrentIndex);
        }

        if (numVoltages > boatMap.batteryBVoltageIndex) {
            bd.batteryAVoltage = dp.voltages.get(boatMap.batteryAVoltageIndex);
            bd.batteryBVoltage = dp.voltages.get(boatMap.batteryBVoltageIndex);
        }

        if (numCurrents > boatMap.motorCurrentIndex) {
            bd.motorCurrent = dp.currents.get(boatMap.motorCurrentIndex);
        }
//        System.out.printf("%f %f %f\n", solarPanelACurrent, solarPanelBCurrent, chargeControllerCurrent);

        bd.speed = ldp.velocity;

        return bd;
    }
}
