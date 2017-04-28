package co.cmsr.optiandroid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonbuckley on 4/26/17.
 */

public class DynamicLineChart {
    List<Entry> entries;
    LineDataSet dataSet;
    LineData lineData;
    LineChart chart;

    String name;
    int maxNumPoints;
    int currNumPoints;

    Handler uiHandler;

    public DynamicLineChart(LineChart lineChart, String name, int maxNumPoints) {
        this.name = name;
        this.chart = lineChart;
        this.maxNumPoints = maxNumPoints;

        currNumPoints = 0;

        entries = new ArrayList<Entry>();
        // Seed with 0 value.
        entries.add(new Entry(0.0f, 0.0f));
        dataSet = new LineDataSet(entries, name);
        lineData = new LineData(dataSet);

        chart.setData(lineData);

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

    public void addPoint(float time, float current) {
        lineData.addEntry(new Entry(time, current), 0);

        currNumPoints ++;

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (currNumPoints > maxNumPoints) {
                    dataSet.removeFirst();
                    dataSet.notifyDataSetChanged();
                    lineData.notifyDataChanged();
                }

                chart.notifyDataSetChanged();
                chart.invalidate();
            }
        });
    }
}
