package co.cmsr.optiandroid.displays;

import android.animation.ArgbEvaluator;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.charts.DynamicBarChart;

/**
 * Created by jonbu on 5/1/2017.
 */

public class SimpleBarDisplay {
    ImageView bar;
    TextView valueDisplay;
    Handler uiHandler;

    float minValue, maxValue;
    float barChartWidthFraction, barChartHeightFraction;
    int valueMinColor, valueMaxColor;
    String suffix;
    String formatString;

    int graphicHeight;
    int maxHeight;
    float startingVerticalBias;
    private static final boolean USE_CHART_VALUES = false;

    DecimalFormat decimalFormatter;
    ArgbEvaluator colorInterpolater;

    public SimpleBarDisplay(
            ImageView bar,
            TextView valueDisplay,
            String name,
            int graphicWidth,
            int graphicHeight,
            float minValue,
            float maxValue,
            int minColor,
            int maxColor,
            float barWidthFraction,
            float barHeightFraction,
            final String formatString,
            final String suffix) {
        this.bar = bar;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueMinColor = minColor;
        this.valueMaxColor = maxColor;
        this.barChartWidthFraction = barWidthFraction;
        this.barChartHeightFraction = barHeightFraction;
        this.graphicHeight = graphicHeight;
        this.formatString = formatString;
        this.suffix = suffix;

        this.valueDisplay = valueDisplay;

        ConstraintLayout.LayoutParams barLayoutParams = (ConstraintLayout.LayoutParams)
                bar.getLayoutParams();
        barLayoutParams.width = (int) (barChartWidthFraction * graphicWidth);
        this.maxHeight = (int) (barChartHeightFraction * graphicHeight);
        this.startingVerticalBias = barLayoutParams.verticalBias;
        bar.setLayoutParams(barLayoutParams);
        bar.requestLayout();

        decimalFormatter = new DecimalFormat(formatString);

        colorInterpolater = new ArgbEvaluator();

        uiHandler = new Handler(Looper.getMainLooper());

        // Initialize display.
        updateDisplay(0.0);
    }

    public void updateDisplay(final double temp) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                float t = (float) Math.max(0, Math.min((temp - minValue) / (maxValue - minValue), 1));
                int hexColor = (int) colorInterpolater.evaluate(
                        t,
                        valueMinColor,
                        valueMaxColor);

                ConstraintLayout.LayoutParams barLayoutParams = (ConstraintLayout.LayoutParams)
                        bar.getLayoutParams();
                int height = (int) (t * maxHeight);
                barLayoutParams.height = height;
                bar.setLayoutParams(barLayoutParams);
                bar.requestLayout();

                valueDisplay.setText(decimalFormatter.format(temp) + suffix);
            }
        });
    }
}
