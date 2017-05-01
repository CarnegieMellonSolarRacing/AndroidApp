package co.cmsr.optiandroid.displays;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.text.DecimalFormat;

import co.cmsr.optiandroid.R;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class ChargeControllerDisplay {
    TextView chargeControllerDisplay;
    Handler uiHandler;
    DecimalFormat decimalFormatter;

    public static final float AMP_MIN = 0.0f;
    public static final float AMP_MAX = 34.0f;
    public static final int AMP_MAX_COLOR = 0x06b703;
    public static final int AMP_MIN_COLOR = 0xff9030;

    public ChargeControllerDisplay(TextView chargeControllerDisplay) {
        this.chargeControllerDisplay = chargeControllerDisplay;
        decimalFormatter = new DecimalFormat("00.0");

        uiHandler = new Handler(Looper.getMainLooper());
        // Initialize to 0
        updateDisplay(0.0);
    }

    public void updateDisplay(Double current) {
        final String currentString = current != null
                ? decimalFormatter.format(current) + " A" : "--";

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                chargeControllerDisplay.setText(currentString);
            }
        });
    }
}
