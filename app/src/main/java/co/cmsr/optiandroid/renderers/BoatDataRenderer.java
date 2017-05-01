package co.cmsr.optiandroid.renderers;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.datastructures.BoatConfig;
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
    TextView batteryAVoltageTextView, batteryBVoltageTextView;
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

    public BoatDataRenderer(Activity activity, final BoatConfig boatConfig) {
        uiInitialized = false;

        final ImageView boatGraphic = (ImageView) activity.findViewById(R.id.boatGraphic);

        batteryAVoltageTextView = (TextView) activity.findViewById(R.id.batteryAVoltage);
        batteryBVoltageTextView = (TextView) activity.findViewById(R.id.batteryBVoltage);

        boatGraphic.post(new Runnable() {
            @Override
            public void run() {
                int graphicWidth = boatGraphic.getMeasuredWidth();
                int graphicHeight = boatGraphic.getMeasuredHeight();

                batteryADisplay = new BatteryDisplay(
                        batteryAVoltageChart,
                        batteryAVoltageTextView,
                        "Battery A",
                        graphicWidth,
                        graphicHeight,
                        boatConfig.batteryMinVoltage,
                        boatConfig.batteryMaxVoltage);

                batteryBDisplay = new BatteryDisplay(
                        batteryBVoltageChart,
                        batteryBVoltageTextView,
                        "Battery B",
                        graphicWidth,
                        graphicHeight,
                        boatConfig.batteryMinVoltage,
                        boatConfig.batteryMaxVoltage);

                uiInitialized = true;
            }
        });

        solarPanelAPowerDisplay = (TextView) activity.findViewById(R.id.solarPanelAPowerDisplay);
        solarPanelACurrentDisplay = (TextView) activity.findViewById(R.id.solarPanelACurrentDisplay);
        solarPanelAVoltageDisplay = (TextView) activity.findViewById(R.id.solarPanelAVoltageDisplay);

//        solarPanelBPowerDisplay = (TextView) activity.findViewById(R.id.solarPanelBPowerDisplay);
//        solarPanelBCurrentDisplay = (TextView) activity.findViewById(R.id.solarPanelBCurrentDisplay);
//        solarPanelBVoltageDisplay = (TextView) activity.findViewById(R.id.solarPanelBVoltageDisplay);

        chargeControllerCurrentDisplay = (TextView) activity.findViewById(R.id.batteryChargeCurrent);

        batteryAVoltageChart = (BarChart) activity.findViewById(R.id.batteryAVoltageChart);
        batteryBVoltageChart = (BarChart) activity.findViewById(R.id.batteryBVoltageChart);

        motorCurrentDisplay = (TextView) activity.findViewById(R.id.motorCurrent);

        boatSpeedDisplay = (TextView) activity.findViewById(R.id.speed);

        solarPanelADisplay = new SolarPanelDisplay(
                solarPanelAPowerDisplay,
                solarPanelACurrentDisplay,
                solarPanelAVoltageDisplay);

//        solarPanelBDisplay = new SolarPanelDisplay(
//                solarPanelBPowerDisplay,
//                solarPanelBCurrentDisplay,
//                solarPanelBVoltageDisplay);

        chargeControllerDisplay = new ChargeControllerDisplay(chargeControllerCurrentDisplay);

        motorDisplay = new MotorDisplay(motorCurrentDisplay);

        speedDisplay = new SpeedDisplay(boatSpeedDisplay);
    }

    @Override
    public void onPacketParsed(float elapsedTime, BoatData dp) {
        if (uiInitialized) {
            solarPanelADisplay.updateDisplay(
                    dp.currentsCalibrated ? dp.solarPanelCurrent : null,
                    dp.solarPanelVoltage);
            chargeControllerDisplay.updateDisplay(
                    dp.currentsCalibrated ? dp.chargeControllerCurrent : null);
            batteryADisplay.updateDisplay(dp.batteryAVoltage);
            batteryBDisplay.updateDisplay(dp.batteryBVoltage);
            motorDisplay.updateDisplay(dp.currentsCalibrated ? dp.motorCurrent : null);
        }
    }
}
