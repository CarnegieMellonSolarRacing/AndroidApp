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

public class SpeedDisplay {
    TextView speedDisplay;
    Handler uiHandler;
    DecimalFormat decimalFormatter;

    public SpeedDisplay(TextView speedDisplay) {
        this.speedDisplay = speedDisplay;
        decimalFormatter = new DecimalFormat("00.0");

        uiHandler = new Handler(Looper.getMainLooper());
    }

    public void updateDisplay(double current) {
        final String currentString = decimalFormatter.format(current) + " mph";

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                speedDisplay.setText(currentString);
            }
        });
    }
}
