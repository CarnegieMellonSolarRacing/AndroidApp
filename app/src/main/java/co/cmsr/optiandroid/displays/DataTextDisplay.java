package co.cmsr.optiandroid.displays;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.text.DecimalFormat;

import co.cmsr.optiandroid.R;
/*
 * Generic text display for data output from the Arduino in float decimal format, one two decimals.
 * For simplicity, have all data displayed in this format.
 */

public class DataTextDisplay {
    TextView textView;
    Handler uiHandler;
    DecimalFormat decimalFormatter;
    String dataSymbol;

    public DataTextDisplay(TextView textView, String dataSymbol) {
        this.textView = textView;
        this.dataSymbol = dataSymbol;
        decimalFormatter = new DecimalFormat("00.00");

        uiHandler = new Handler(Looper.getMainLooper());

        // Initialize to 0.
        updateDisplay(0.0);
    }

    public void updateDisplay(Double value) {
        /*
            This function will serve as a callback that gets called whenever new data is available,
            and this will update the values shown on the app.
         */

        // Convert input value into string with symbol at the end
        // Example symbols: ºF, ºC, A(amps), V(volts), W(watts)
        final String dataString;
        if (value != null) {
            dataString  = decimalFormatter.format(value) + dataSymbol;
        } else {
            dataString = "No Value";
        }

        // the actual code that updates the display on the app
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(dataString);
            }
        });
    }

    public void debugPrint(String val) {
        final String dataString;
        if (val != null) dataString = val;
        else dataString = "No value";

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(dataString);
            }
        });
    }
}