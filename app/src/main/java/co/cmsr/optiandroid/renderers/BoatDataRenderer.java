package co.cmsr.optiandroid.renderers;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.datastructures.BoatConfig;
import co.cmsr.optiandroid.datastructures.BoatData;
import co.cmsr.optiandroid.displays.DataTextDisplay;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatDataRenderer implements DataRenderer {
    TextView solarPanelPowerTextView;
    TextView batteryChargeTextView, batteryChargePercentTextView, batteryTempTextView;
    TextView motorPowerTextView;
    TextView boatSpeedTextView;

    String power_symbol = "W";
    String charge_symbol = "C";
    String percent_symbol = "%";
    String temp_symbol = "ºF";  // Fahrenheit
    String speed_symbol = "mph";

    DataTextDisplay solarPanelDisplay, batteryChargeDisplay, batteryChargePercentDisplay,
            batteryTempDisplay, motorPowerDisplay, boatSpeedDisplay;

    volatile boolean uiInitialized;

    public BoatDataRenderer(Activity activity, final BoatConfig boatConfig) {
        uiInitialized = false;

        final ImageView boatGraphic = (ImageView) activity.findViewById(R.id.boatGraphic);

        // Text views that need to be integrated with the actual UI displays
        solarPanelPowerTextView = (TextView) activity.findViewById(R.id.solarPanelPower);
        batteryChargeTextView = (TextView) activity.findViewById(R.id.batteryCharge);
        batteryChargePercentTextView = (TextView) activity.findViewById(R.id.batteryChargePercent);
        batteryTempTextView = (TextView) activity.findViewById(R.id.batteryTemp);
        motorPowerTextView = (TextView) activity.findViewById(R.id.motorPower);
        boatSpeedTextView = (TextView) activity.findViewById(R.id.speed);

        solarPanelDisplay = new DataTextDisplay(solarPanelPowerTextView, power_symbol);
        batteryChargeDisplay = new DataTextDisplay(batteryChargeTextView, charge_symbol);
        batteryChargePercentDisplay = new DataTextDisplay(batteryChargePercentTextView, percent_symbol);
        batteryTempDisplay = new DataTextDisplay(batteryTempTextView, temp_symbol);
        motorPowerDisplay = new DataTextDisplay(motorPowerTextView, power_symbol);
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
    public void renderData(float elapsedTime, BoatData dp) {
        solarPanelDisplay.updateDisplay(dp.solarPanelPower);
        batteryChargeDisplay.updateDisplay(dp.batteryCharge);
        batteryChargePercentDisplay.updateDisplay(dp.batteryChargePercent);
        batteryTempDisplay.updateDisplay(dp.batteryTemp);
        motorPowerDisplay.updateDisplay(dp.motorPower);
        boatSpeedDisplay.updateDisplay(dp.boatSpeed);
    }

    @Override
    public void printDebug(String val) {

    }
}