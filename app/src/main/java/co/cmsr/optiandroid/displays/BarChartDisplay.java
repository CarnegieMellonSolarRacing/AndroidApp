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

import co.cmsr.optiandroid.R;
import co.cmsr.optiandroid.charts.DynamicBarChart;

/**
 * Created by jonbuckley on 4/30/17.
 */

public class BarChartDisplay {
    BarChart barChart;
    TextView valueDisplay;
    DynamicBarChart dynamicBarChart;
    Handler uiHandler;
    volatile List<Double> values;
    float minValue, maxValue;
    float barChartWidthFraction, barChartHeightFraction;
    int valueMinColor, valueMaxColor;
    String suffix;
    String formatString;

    private static final boolean USE_CHART_VALUES = false;

    DecimalFormat decimalFormatter;
    ArgbEvaluator colorInterpolater;

    public BarChartDisplay(
            BarChart barChart,
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
        this.barChart = barChart;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.valueMinColor = minColor;
        this.valueMaxColor = maxColor;
        this.barChartWidthFraction = barWidthFraction;
        this.barChartHeightFraction = barHeightFraction;
        this.formatString = formatString;
        this.suffix = suffix;

        this.valueDisplay = valueDisplay;

        ConstraintLayout.LayoutParams barChartLayoutParams = (ConstraintLayout.LayoutParams)
                barChart.getLayoutParams();
        // Switch from a verticalBias approach to a margin approach.
        float offsetFromBottom = (1.0f - barChartLayoutParams.verticalBias) * (graphicHeight - barChartLayoutParams.height);
        barChartLayoutParams.verticalBias = 1.0f;
        barChartLayoutParams.bottomMargin = (int) offsetFromBottom;

        // Adjust width and height.
        barChartLayoutParams.width = (int) (barChartWidthFraction * graphicWidth);
        barChartLayoutParams.height = (int) (barChartHeightFraction * graphicHeight);
        barChart.setLayoutParams(barChartLayoutParams);
        barChart.notifyDataSetChanged();
        barChart.requestLayout();

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Value");

        dynamicBarChart = new DynamicBarChart(
                this.barChart,
                labels,
                name,
                minValue,
                maxValue);

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

        decimalFormatter = new DecimalFormat(formatString);

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
                    return decimalFormatter.format(value) + suffix;
                }
            });
            float secondaryTextSize = barChart.getResources().getDimension(R.dimen.text_secondary);
            dynamicBarChart.barData.setValueTextSize(secondaryTextSize / 1.0f);
        } else {
            dynamicBarChart.barData.setDrawValues(false);
        }

        values = new ArrayList<>();
        values.add(new Double(0.0));

        colorInterpolater = new ArgbEvaluator();

        uiHandler = new Handler(Looper.getMainLooper());

        // Initialize display.
        updateDisplay(0.0);
    }

    public void updateDisplay(final double temp) {
        values.set(0, new Double(temp));

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                float t = (float) Math.max(0, Math.min((temp - minValue) / (maxValue - minValue), 1));
                int hexColor = (int) colorInterpolater.evaluate(
                        t,
                        valueMinColor,
                        valueMaxColor);
                int colorToSet = ColorTemplate.rgb(Integer.toHexString(hexColor));
                dynamicBarChart.dataSet.setColor(colorToSet);
                dynamicBarChart.updateValues(values);

                valueDisplay.setText(decimalFormatter.format(temp) + suffix);
            }
        });
    }
}
