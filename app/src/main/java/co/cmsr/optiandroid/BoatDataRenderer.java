package co.cmsr.optiandroid;

import android.app.Activity;
import android.widget.TextView;

import co.cmsr.optiandroid.displays.ChargeControllerDisplay;
import co.cmsr.optiandroid.displays.SolarPanelDisplay;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BoatDataRenderer implements DataRenderer {
    TextView solarPanelAPowerDisplay, solarPanelACurrentDisplay, solarPanelAVoltageDisplay;
    TextView solarPanelBPowerDisplay, solarPanelBCurrentDisplay, solarPanelBVoltageDisplay;
    TextView chargeControllerCurrentDisplay;

    SolarPanelDisplay solarPanelADisplay;
    SolarPanelDisplay solarPanelBDisplay;
    ChargeControllerDisplay chargeControllerDisplay;

    public BoatDataRenderer(Activity activity) {
        solarPanelAPowerDisplay = (TextView) activity.findViewById(R.id.solarPanelAPowerDisplay);
        solarPanelACurrentDisplay = (TextView) activity.findViewById(R.id.solarPanelACurrentDisplay);
        solarPanelAVoltageDisplay = (TextView) activity.findViewById(R.id.solarPanelAVoltageDisplay);

        solarPanelBPowerDisplay = (TextView) activity.findViewById(R.id.solarPanelBPowerDisplay);
        solarPanelBCurrentDisplay = (TextView) activity.findViewById(R.id.solarPanelBCurrentDisplay);
        solarPanelBVoltageDisplay = (TextView) activity.findViewById(R.id.solarPanelBVoltageDisplay);

        chargeControllerCurrentDisplay = (TextView) activity.findViewById(R.id.batteryChargeCurrent);

        solarPanelADisplay = new SolarPanelDisplay(
                solarPanelAPowerDisplay,
                solarPanelACurrentDisplay,
                solarPanelAVoltageDisplay);

        solarPanelBDisplay = new SolarPanelDisplay(
                solarPanelBPowerDisplay,
                solarPanelBCurrentDisplay,
                solarPanelBVoltageDisplay);

        chargeControllerDisplay = new ChargeControllerDisplay(chargeControllerCurrentDisplay);
    }

    @Override
    public void onPacketParsed(float elapsedTime, BoatData dp) {
        solarPanelADisplay.updateDisplay(dp.solarPanelACurrent, dp.solarPanelAVoltage);
        solarPanelBDisplay.updateDisplay(dp.solarPanelBCurrent, dp.solarPanelBVoltage);
        chargeControllerDisplay.updateDisplay(dp.chargeControllerCurrent);
    }
}
