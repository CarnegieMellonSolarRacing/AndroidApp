package co.cmsr.optiandroid.renderers;

import android.app.Activity;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.datastructures.BoatConfig;
import co.cmsr.optiandroid.datastructures.DataPacket;
import co.cmsr.optiandroid.displays.DataTextDisplay;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatDataRenderer implements DataRenderer {
    TextView solarPanelPowerTextView;
    TextView batteryChargeTextView, batteryChargePercentTextView, batteryTempTextView;
    TextView boatSpeedTextView;

    String power_symbol = "W";
    String charge_symbol = "C";
    String percent_symbol = "%";
    String temp_symbol = "ºF";  // Fahrenheit
    String speed_symbol = "mph";

    DataTextDisplay solarPanelDisplay, batteryChargeDisplay, batteryChargePercentDisplay,
            batteryTempDisplay, motorPowerDisplay, boatSpeedDisplay;

    volatile boolean uiInitialized;

    public BoatDataRenderer(Activity activity, final BoatConfig boatConfig, Double charge_left) {
        uiInitialized = false;

        final ImageView boatGraphic = (ImageView) activity.findViewById(R.id.boatGraphic);

        // Text views that need to be integrated with the actual UI displays
        solarPanelPowerTextView = (TextView) activity.findViewById(R.id.solarPanelPower);
        batteryChargeTextView = (TextView) activity.findViewById(R.id.batteryCharge);
        batteryChargePercentTextView = (TextView) activity.findViewById(R.id.batteryChargePercent);
        batteryTempTextView = (TextView) activity.findViewById(R.id.batteryTemp);
        boatSpeedTextView = (TextView) activity.findViewById(R.id.speed);

        solarPanelDisplay = new DataTextDisplay(solarPanelPowerTextView, power_symbol);
        batteryChargeDisplay = new DataTextDisplay(batteryChargeTextView, charge_symbol);
        batteryChargeDisplay.updateDisplay(charge_left);
        batteryChargePercentDisplay = new DataTextDisplay(batteryChargePercentTextView, percent_symbol);
        batteryTempDisplay = new DataTextDisplay(batteryTempTextView, temp_symbol);
        boatSpeedDisplay = new DataTextDisplay(boatSpeedTextView, speed_symbol);


        boatGraphic.post(new Runnable() {
            // This does nothing right now, not sure what it does
            @Override
            public void run() {
                int graphicWidth = boatGraphic.getMeasuredWidth();
                int graphicHeight = boatGraphic.getMeasuredHeight();
                uiInitialized = true;
            }
        });
    }

    @Override
    public void renderData(LinkedList<DataPacket> dp_list) {
        if (!dp_list.isEmpty()) {
            DataPacket dp = dp_list.getLast();
            solarPanelDisplay.updateDisplay(dp.solar_charge_rate);
            batteryChargeDisplay.updateDisplay(dp.charge_left);
            batteryChargePercentDisplay.updateDisplay(dp.battery_charge_percent);
            batteryTempDisplay.updateDisplay(dp.battery_temp);
            boatSpeedDisplay.updateDisplay(dp.boatSpeed);
        }
    }

    @Override
    public void printDebug(String val) {
        solarPanelDisplay.debugPrint(val);
    }
}