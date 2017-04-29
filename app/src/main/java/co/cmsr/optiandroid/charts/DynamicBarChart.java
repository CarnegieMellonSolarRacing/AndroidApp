package co.cmsr.optiandroid.charts;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.entries;

/**
 * Created by jonbuckley on 4/26/17.
 */

public class DynamicBarChart {
    List<BarEntry> entries;
    public BarDataSet dataSet;
    public BarData barData;
    public BarChart chart;

    float minValue, maxValue;

    String name;

    Handler uiHandler;

    public DynamicBarChart(
            BarChart barChart,
            List<String> labels,
            String name,
            float minValue,
            float maxValue) {
        this.name = name;
        this.chart = barChart;

        entries = new ArrayList<BarEntry>();
        int counter = 0;
        for (String label : labels) {
            BarEntry entry = new BarEntry(counter, 0.0f);
            entries.add(entry);
            counter++;
        }

        dataSet = new BarDataSet(entries, name);
        barData = new BarData(dataSet);
        barData.setBarWidth(1.0f);
        chart.setData(barData);
        chart.setFitBars(true);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisMinimum(minValue);
        yAxis.setAxisMaximum(maxValue);
        yAxis = chart.getAxisRight();
        yAxis.setAxisMinimum(minValue);
        yAxis.setAxisMaximum(maxValue);

        uiHandler = new Handler(Looper.getMainLooper());
        updateUI();
    }

    private void updateUI() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                chart.invalidate();
            }
        });
    }

    public void updateValues(List<Double> values) {
        int counter = 0;
        for (Double val : values) {
            if (counter >= entries.size()) {
                break;
            }
            // Update entry with new value.
            entries.get(counter).setY(val.floatValue());
            counter++;
        }
        dataSet.notifyDataSetChanged();
        barData.notifyDataChanged();
        chart.notifyDataSetChanged();

        updateUI();
    }
}
