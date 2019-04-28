package co.cmsr.optiandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.cmsr.optiandroid.logging.Logger;
import co.cmsr.optiandroid.replay.LogReplayer;

public class DataReviewActivity extends AppCompatActivity {
    LineChart batteryVoltagesChart;
    LineChart currentsChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        batteryVoltagesChart = (LineChart) findViewById(R.id.batteryVoltagesChart);
        currentsChart = (LineChart) findViewById(R.id.currentsChart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeCharts();
    }

    private void initializeCharts() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> batteryAVoltageEntries = new ArrayList<Entry>();
                batteryAVoltageEntries.add(new Entry(0, 0));
                ArrayList<Entry> batteryBVoltageEntries = new ArrayList<Entry>();
                batteryBVoltageEntries.add(new Entry(0, 0));

                LineDataSet batteryAVoltages = new LineDataSet(batteryAVoltageEntries, "Battery A");
                batteryAVoltages.setColor(ColorTemplate.rgb("77f442"));
                LineDataSet batteryBVoltages = new LineDataSet(batteryBVoltageEntries, "Battery B");
                batteryBVoltages.setColors(ColorTemplate.rgb("5c41f4"));

                LineData batteryVoltageData = new LineData();
                batteryVoltageData.addDataSet(batteryAVoltages);
                batteryVoltageData.addDataSet(batteryBVoltages);

                Description batteryChartDescription = new Description();
                batteryChartDescription.setText("Battery Voltages");
                batteryVoltagesChart.setDescription(batteryChartDescription);
                batteryVoltagesChart.setData(batteryVoltageData);

                ArrayList<Entry> solarPanelCurrentEntries = new ArrayList<Entry>();
                solarPanelCurrentEntries.add(new Entry(0, 0));
                ArrayList<Entry> motorCurrentEntries = new ArrayList<Entry>();
                motorCurrentEntries.add(new Entry(0, 0));

                LineDataSet solarPanelCurrents = new LineDataSet(solarPanelCurrentEntries, "Solar Panel");
                solarPanelCurrents.setColor(ColorTemplate.rgb("d3d323"));
                LineDataSet motorCurrents = new LineDataSet(motorCurrentEntries, "Motor Current");
                motorCurrents.setColor(ColorTemplate.rgb("dd5318"));

                LineData currentData = new LineData();
                currentData.addDataSet(solarPanelCurrents);
                currentData.addDataSet(motorCurrents);

                Description currentsChartDescription = new Description();
                currentsChartDescription.setText("Power Gain & Draw");
                currentsChart.setDescription(currentsChartDescription);
                currentsChart.setData(currentData);
            }
        });
    }

    public void openLogFileButtonClicked(View view) {
        FileChooser fileChooser = new FileChooser(this, Logger.getStorageDir());
        fileChooser.setExtension(".log");
        fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(File file) {
                return;
            }
        });
        fileChooser.showDialog();
    }

}
