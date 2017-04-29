package co.cmsr.optiandroid.datastructures;

import android.app.Activity;
import android.content.Context;

import co.cmsr.optiandroid.LocationTracker;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatData {
    public static final int SOLAR_PANEL_A_VOLTAGE_INDEX = 0;
    public static final int SOLAR_PANEL_B_VOLTAGE_INDEX = 1;

    public static final int SOLAR_PANEL_A_CURRENT_INDEX = 0;
    public static final int SOLAR_PANEL_B_CURRENT_INDEX = 1;

    public static final int CHARGE_CONTROLLER_CURRENT_INDEX = 2;

    public static final int BATTERY_A_VOLTAGE_INDEX = 2;
    public static final int BATTERY_B_VOLTAGE_INDEX = 3;

    public static final int MOTOR_CURRENT_INDEX = 3;

    public double solarPanelACurrent;
    public double solarPanelAVoltage;
    public double solarPanelBCurrent;
    public double solarPanelBVoltage;

    public double chargeControllerCurrent;

    public double batteryAVoltage;
    public double batteryBVoltage;

    public double motorCurrent;

    public double speed;

    public BoatData(Context context, DataPacket dp) {
        int numCurrents = dp.currents.size();
        int numVoltages = dp.voltages.size();

        if (numCurrents > SOLAR_PANEL_B_CURRENT_INDEX) {
            solarPanelACurrent = dp.currents.get(SOLAR_PANEL_A_CURRENT_INDEX);
            solarPanelBCurrent = dp.currents.get(SOLAR_PANEL_B_CURRENT_INDEX);
        }

        if (numVoltages > SOLAR_PANEL_B_VOLTAGE_INDEX) {
            solarPanelAVoltage = dp.voltages.get(SOLAR_PANEL_A_VOLTAGE_INDEX);
            solarPanelBVoltage = dp.voltages.get(SOLAR_PANEL_B_VOLTAGE_INDEX);
        }

        if (numCurrents > CHARGE_CONTROLLER_CURRENT_INDEX) {
            chargeControllerCurrent = dp.currents.get(CHARGE_CONTROLLER_CURRENT_INDEX);
        }

        if (numVoltages > BATTERY_B_VOLTAGE_INDEX) {
            batteryAVoltage = dp.voltages.get(BATTERY_A_VOLTAGE_INDEX);
            batteryBVoltage = dp.voltages.get(BATTERY_B_VOLTAGE_INDEX);
        }

        if (numCurrents > MOTOR_CURRENT_INDEX) {
            motorCurrent = dp.currents.get(MOTOR_CURRENT_INDEX);
        }
//        System.out.printf("%f %f %f\n", solarPanelACurrent, solarPanelBCurrent, chargeControllerCurrent);

        speed = -1.0f;
        LocationTracker tracker = LocationTracker.getInstance((Activity) context);
        if (tracker.isEnabled()) {
            speed = tracker.getSpeedMph();
        }
    }
}
