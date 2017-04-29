package co.cmsr.optiandroid.renderers;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.datastructures.BoatData;
import co.cmsr.optiandroid.displays.BatteryDisplay;
import co.cmsr.optiandroid.displays.ChargeControllerDisplay;
import co.cmsr.optiandroid.displays.MotorDisplay;
import co.cmsr.optiandroid.displays.SolarPanelDisplay;
import co.cmsr.optiandroid.displays.SpeedDisplay;
import co.cmsr.optiandroid.renderers.DataRenderer;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatDataRenderer implements DataRenderer {
    TextView solarPanelAPowerDisplay, solarPanelACurrentDisplay, solarPanelAVoltageDisplay;
    TextView solarPanelBPowerDisplay, solarPanelBCurrentDisplay, solarPanelBVoltageDisplay;
    TextView chargeControllerCurrentDisplay;
    BarChart batteryAVoltageChart, batteryBVoltageChart;
    TextView motorCurrentDisplay;
    TextView boatSpeedDisplay;

    SolarPanelDisplay solarPanelADisplay;
    SolarPanelDisplay solarPanelBDisplay;
    ChargeControllerDisplay chargeControllerDisplay;
    BatteryDisplay batteryADisplay;
    BatteryDisplay batteryBDisplay;
    MotorDisplay motorDisplay;
    SpeedDisplay speedDisplay;

    volatile boolean uiInitialized;

    public BoatDataRenderer(Activity activity) {
        uiInitialized = false;

        final ImageView boatGraphic = (ImageView) activity.findViewById(R.id.boatGraphic);

        boatGraphic.post(new Runnable() {
            @Override
            public void run() {
                int graphicWidth = boatGraphic.getMeasuredWidth();
                int graphicHeight = boatGraphic.getMeasuredHeight();

                batteryADisplay = new BatteryDisplay(
                        batteryAVoltageChart,
                        "Battery A",
                        graphicWidth,
                        graphicHeight);

                batteryBDisplay = new BatteryDisplay(
                        batteryBVoltageChart,
                        "Battery B",
                        graphicWidth,
                        graphicHeight);

                uiInitialized = true;
            }
        });

        solarPanelAPowerDisplay = (TextView) activity.findViewById(R.id.solarPanelAPowerDisplay);
        solarPanelACurrentDisplay = (TextView) activity.findViewById(R.id.solarPanelACurrentDisplay);
        solarPanelAVoltageDisplay = (TextView) activity.findViewById(R.id.solarPanelAVoltageDisplay);

        solarPanelBPowerDisplay = (TextView) activity.findViewById(R.id.solarPanelBPowerDisplay);
        solarPanelBCurrentDisplay = (TextView) activity.findViewById(R.id.solarPanelBCurrentDisplay);
        solarPanelBVoltageDisplay = (TextView) activity.findViewById(R.id.solarPanelBVoltageDisplay);

        chargeControllerCurrentDisplay = (TextView) activity.findViewById(R.id.batteryChargeCurrent);

        batteryAVoltageChart = (BarChart) activity.findViewById(R.id.batteryAVoltage);
        batteryBVoltageChart = (BarChart) activity.findViewById(R.id.batteryBVoltage);

        motorCurrentDisplay = (TextView) activity.findViewById(R.id.motorCurrent);

        boatSpeedDisplay = (TextView) activity.findViewById(R.id.speed);

        solarPanelADisplay = new SolarPanelDisplay(
                solarPanelAPowerDisplay,
                solarPanelACurrentDisplay,
                solarPanelAVoltageDisplay);

        solarPanelBDisplay = new SolarPanelDisplay(
                solarPanelBPowerDisplay,
                solarPanelBCurrentDisplay,
                solarPanelBVoltageDisplay);

        chargeControllerDisplay = new ChargeControllerDisplay(chargeControllerCurrentDisplay);

        motorDisplay = new MotorDisplay(motorCurrentDisplay);

        speedDisplay = new SpeedDisplay(boatSpeedDisplay);
    }

    @Override
    public void onPacketParsed(float elapsedTime, BoatData dp) {
        if (uiInitialized) {
            solarPanelADisplay.updateDisplay(dp.solarPanelACurrent, dp.solarPanelAVoltage);
            solarPanelBDisplay.updateDisplay(dp.solarPanelBCurrent, dp.solarPanelBVoltage);
            chargeControllerDisplay.updateDisplay(dp.chargeControllerCurrent);
            batteryADisplay.updateDisplay(dp.batteryAVoltage);
            batteryBDisplay.updateDisplay(dp.batteryBVoltage);
            motorDisplay.updateDisplay(dp.motorCurrent);
        }
    }
}
