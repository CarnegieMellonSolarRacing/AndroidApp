package co.cmsr.optiandroid.datastructures;

/**
 * Created by jonbu on 4/30/2017.
 */

public class BoatMap {
    // BOAT MAP
    private static final int SOLAR_PANEL_CURRENT_INDEX = 1;
    private static final int BATTERY_CHARGER_CURRENT_INDEX = 2;
    private static final int MOTOR_CURRENT_INDEX = 0;

    private static final int BATTERY_A_VOLTAGE_INDEX = 0;
    private static final int BATTERY_B_VOLTAGE_INDEX = 1;
    private static final int SOLAR_PANEL_VOLTAGE_INDEX = 2;

    private static final int PANEL_A_TEMP_INDEX = 0;
    private static final int PANEL_B_TEMP_INDEX = 1;
    // END BOAT MAP

    public int solarPanelVoltageIndex;
    public int solarPanelCurrentIndex;
    public int batteryAVoltageIndex;
    public int batteryBVoltageIndex;
    public int chargeControllerCurrentIndex;
    public int motorCurrentIndex;
    public int panelATempIndex;
    public int panelBTempIndex;

    public BoatMap(boolean fillWithDefaults) {
        if (fillWithDefaults) {
            solarPanelVoltageIndex = SOLAR_PANEL_VOLTAGE_INDEX;
            solarPanelCurrentIndex = SOLAR_PANEL_CURRENT_INDEX;
            batteryAVoltageIndex = BATTERY_A_VOLTAGE_INDEX;
            batteryBVoltageIndex = BATTERY_B_VOLTAGE_INDEX;
            chargeControllerCurrentIndex = BATTERY_CHARGER_CURRENT_INDEX;
            motorCurrentIndex = MOTOR_CURRENT_INDEX;
            panelATempIndex = PANEL_A_TEMP_INDEX;
            panelBTempIndex = PANEL_B_TEMP_INDEX;
        }
    }
}