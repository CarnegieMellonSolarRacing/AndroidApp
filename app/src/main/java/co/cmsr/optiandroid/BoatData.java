package co.cmsr.optiandroid;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatData {
    public static final int SOLAR_PANEL_A_VOLTAGE_INDEX = 0;
    public static final int SOLAR_PANEL_B_VOLTAGE_INDEX = 1;

    public static final int SOLAR_PANEL_A_CURRENT_INDEX = 0;
    public static final int SOLAR_PANEL_B_CURRENT_INDEX = 1;

    public static final int CHARGE_CONTROLLER_CURRENT_INDEX = 2;

    public double solarPanelACurrent;
    public double solarPanelAVoltage;
    public double solarPanelBCurrent;
    public double solarPanelBVoltage;

    public double chargeControllerCurrent;

    public BoatData(DataPacket dp) {
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
    }
}
