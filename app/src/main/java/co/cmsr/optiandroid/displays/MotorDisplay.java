package co.cmsr.optiandroid.displays;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.text.DecimalFormat;

import co.cmsr.optiandroid.R;
/**
 * Created by jonbuckley on 4/28/17.
 */

public class MotorDisplay {
    TextView motorCurrentDisplay;
    Handler uiHandler;
    DecimalFormat decimalFormatter;

    public MotorDisplay(TextView motorCurrentDisplay) {
        this.motorCurrentDisplay = motorCurrentDisplay;
        decimalFormatter = new DecimalFormat("00.0");

        uiHandler = new Handler(Looper.getMainLooper());

        // Initialize to 0.
        updateDisplay(0.0);
    }

    public void updateDisplay(double current) {
        final String currentString = decimalFormatter.format(current) + " A";

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                motorCurrentDisplay.setText(currentString);
            }
        });
    }
}
