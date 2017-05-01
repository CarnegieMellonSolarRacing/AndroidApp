package co.cmsr.optiandroid.datastructures;

import android.app.Activity;
import android.content.Context;

import co.cmsr.optiandroid.LocationTracker;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatData {
    public boolean currentsCalibrated;

    public double solarPanelACurrent;
    public double solarPanelAVoltage;
    public double solarPanelBCurrent;
    public double solarPanelBVoltage;

    public double chargeControllerCurrent;

    public double batteryAVoltage;
    public double batteryBVoltage;

    public double motorCurrent;

    public double speed;

    public BoatData(
            Context context,
            boolean currentsCalibrated,
            DataPacket dp,
            LocalDataPacket ldp) {
        this.currentsCalibrated = currentsCalibrated;

        int numCurrents = dp.currents.size();
        int numVoltages = dp.voltages.size();

        if (numCurrents > BoatMap.SOLAR_PANEL_B_CURRENT_INDEX) {
            solarPanelACurrent = dp.currents.get(BoatMap.SOLAR_PANEL_A_CURRENT_INDEX);
            solarPanelBCurrent = dp.currents.get(BoatMap.SOLAR_PANEL_B_CURRENT_INDEX);
        }

        if (numVoltages > BoatMap.SOLAR_PANEL_B_VOLTAGE_INDEX) {
            solarPanelAVoltage = dp.voltages.get(BoatMap.SOLAR_PANEL_A_VOLTAGE_INDEX);
            solarPanelBVoltage = dp.voltages.get(BoatMap.SOLAR_PANEL_B_VOLTAGE_INDEX);
        }

        if (numCurrents > BoatMap.CHARGE_CONTROLLER_CURRENT_INDEX) {
            chargeControllerCurrent = dp.currents.get(BoatMap.CHARGE_CONTROLLER_CURRENT_INDEX);
        }

        if (numVoltages > BoatMap.BATTERY_B_VOLTAGE_INDEX) {
            batteryAVoltage = dp.voltages.get(BoatMap.BATTERY_A_VOLTAGE_INDEX);
            batteryBVoltage = dp.voltages.get(BoatMap.BATTERY_B_VOLTAGE_INDEX);
        }

        if (numCurrents > BoatMap.MOTOR_CURRENT_INDEX) {
            motorCurrent = dp.currents.get(BoatMap.MOTOR_CURRENT_INDEX);
        }
//        System.out.printf("%f %f %f\n", solarPanelACurrent, solarPanelBCurrent, chargeControllerCurrent);

        speed = ldp.velocity;
    }
}
