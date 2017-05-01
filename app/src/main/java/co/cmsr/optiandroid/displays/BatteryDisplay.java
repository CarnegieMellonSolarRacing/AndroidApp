package co.cmsr.optiandroid.displays;

import android.animation.ArgbEvaluator;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
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

import co.cmsr.optiandroid.charts.DynamicBarChart;
import co.cmsr.optiandroid.R;

/**
 * Created by jonbuckley on 4/28/17.
 */

public class BatteryDisplay {
    BarChart barChart;
    TextView voltageDisplay;
    DynamicBarChart dynamicBarChart;
    Handler uiHandler;
    volatile List<Double> voltages;
    float voltMin, voltMax;

    private static final boolean USE_CHART_VALUES = false;

    public static final float BAR_CHART_WIDTH_FRACTION = 0.37f;
    public static final float BAR_CHART_HEIGHT_FRACTION = 0.142f;
    public static final int VOLT_MAX_COLOR = 0x06b703;
    public static final int VOLT_MIN_COLOR = 0xff9030;

    DecimalFormat decimalFormatter;
    ArgbEvaluator colorInterpolater;

    public BatteryDisplay(
            BarChart barChart,
            TextView voltageDisplay,
            String name,
            int graphicWidth,
            int graphicHeight,
            float voltMin,
            float voltMax) {
        this.barChart = barChart;
        this.voltageDisplay = voltageDisplay;

        ConstraintLayout.LayoutParams barChartLayoutParams = (ConstraintLayout.LayoutParams)
                barChart.getLayoutParams();
        barChartLayoutParams.width = (int) (BAR_CHART_WIDTH_FRACTION * graphicWidth);
        barChartLayoutParams.height = (int) (BAR_CHART_HEIGHT_FRACTION * graphicHeight);
        barChart.setLayoutParams(barChartLayoutParams);
        barChart.notifyDataSetChanged();
        barChart.requestLayout();

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Voltage");

        dynamicBarChart = new DynamicBarChart(
                this.barChart,
                labels,
                name,
                voltMin,
                voltMax);
        // Disable borders and background.
        dynamicBarChart.chart.setDrawGridBackground(false);
        dynamicBarChart.chart.setDrawBorders(false);
        dynamicBarChart.chart.getDescription().setEnabled(false);
        dynamicBarChart.chart.getLegend().setEnabled(false);
        dynamicBarChart.chart.getAxis(YAxis.AxisDependency.LEFT).setEnabled(false);
        dynamicBarChart.chart.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);
        dynamicBarChart.chart.getXAxis().setEnabled(false);
        // Fill entire graph.
        dynamicBarChart.chart.setFitBars(false);

        decimalFormatter = new DecimalFormat("00.0");

        if (USE_CHART_VALUES) {
            // Put labels below chart.
            dynamicBarChart.chart.setDrawValueAboveBar(false);
            // Set value formatting.
            dynamicBarChart.barData.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(
                        float value,
                        Entry entry,
                        int dataSetIndex,
                        ViewPortHandler viewPortHandler) {
                    return decimalFormatter.format(value) + " V";
                }
            });
            float secondaryTextSize = barChart.getResources().getDimension(R.dimen.text_secondary);
            dynamicBarChart.barData.setValueTextSize(secondaryTextSize / 1.0f);
        } else {
            dynamicBarChart.barData.setDrawValues(false);
        }

        // Re-layout.
        dynamicBarChart.chart.requestLayout();

        voltages = new ArrayList<>();
        voltages.add(new Double(0.0));

        colorInterpolater = new ArgbEvaluator();

        uiHandler = new Handler(Looper.getMainLooper());

        // Initialize display.
        updateDisplay(0.0);
    }

    public void updateDisplay(final double voltage) {
        voltages.set(0, new Double(voltage));

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                float t = (float) voltage / (voltMax - voltMin);
                int hexColor = (int) colorInterpolater.evaluate(
                        t,
                        VOLT_MIN_COLOR,
                        VOLT_MAX_COLOR);
                int colorToSet = ColorTemplate.rgb(Integer.toHexString(hexColor));
                dynamicBarChart.dataSet.setColor(colorToSet);
                dynamicBarChart.updateValues(voltages);

                voltageDisplay.setText(decimalFormatter.format(voltage) + " V");
            }
        });
    }
}
