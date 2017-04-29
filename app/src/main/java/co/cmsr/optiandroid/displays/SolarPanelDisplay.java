package co.cmsr.optiandroid.displays;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class SolarPanelDisplay {
    TextView powerDisplay, currentDisplay, voltageDisplay;
    Handler uiHandler;
    DecimalFormat decimalFormatter;

    public SolarPanelDisplay(TextView power, TextView current, TextView voltage) {
        powerDisplay = power;
        currentDisplay = current;
        voltageDisplay = voltage;

        decimalFormatter = new DecimalFormat("00.0");

        uiHandler = new Handler(Looper.getMainLooper());

        // Initialize to 0.
        updateDisplay(0.0, 0.0);
    }

    public void updateDisplay(double current, double voltage) {
        double power = current * voltage;
        final String powerString = decimalFormatter.format(power) + " W";
        final String currentString = decimalFormatter.format(current) + " A";
        final String voltageString = decimalFormatter.format(voltage) + " V";

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                powerDisplay.setText(powerString);
                currentDisplay.setText(currentString);
                voltageDisplay.setText(voltageString);
            }
        });
    }
}
