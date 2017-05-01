package co.cmsr.optiandroid.datastructures;

/**
 * Created by jonbuckley on 4/30/17.
 */

public class BoatConfig {
    // BOAT CONFIG
    private static final int NUM_CURRENTS = 3;
    private static final int NUM_VOLTAGES = 3;
    private static final int NUM_TEMPERATURES = 3;

    private static final float BATTERY_VOLT_MIN = 0.0f, BATTERY_VOLT_MAX = 17.0f;
    private static final float PANEL_TEMP_MIN = 15.0f, PANEL_TEMP_MAX = 40.0f;
    // END BOAT CONFIG

    public int numCurrents;
    public int numVoltages;
    public int numTemperatures;

    public float batteryMaxVoltage;
    public float batteryMinVoltage;

    public float panelMaxTemperature;
    public float panelMinTemperature;

    public BoatConfig(boolean fillWithDefaults) {
        if (fillWithDefaults) {
            numCurrents = NUM_CURRENTS;
            numVoltages = NUM_VOLTAGES;
            numTemperatures = NUM_TEMPERATURES;

            batteryMaxVoltage = BATTERY_VOLT_MAX;
            batteryMinVoltage = BATTERY_VOLT_MIN;

            panelMaxTemperature = PANEL_TEMP_MAX;
            panelMinTemperature = PANEL_TEMP_MIN;
        }
    }
}
