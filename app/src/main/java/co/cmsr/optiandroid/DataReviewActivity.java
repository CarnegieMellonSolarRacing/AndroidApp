package co.cmsr.optiandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.cmsr.optiandroid.logging.Logger;

public class DataReviewActivity extends AppCompatActivity {
    LineChart batteryVoltagesChart;
    LineChart currentsChart;

    private String chosenFile;
    private String[] fileList;
    public static final String FTYPE = ".txt";
    public static final int DIALOG_LOAD_FILE = 1000;

    private void loadFileList(File logDir) {
        if(logDir.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }
            };
            fileList = logDir.list(filter);
        }
        else {
            fileList= new String[0];
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if(fileList == null) {
                    Log.e(TAG, "Showing file picker before loading the file list");
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(fileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        chosenFile = fileList[which];
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        batteryVoltagesChart = (LineChart) findViewById(R.id.batteryVoltagesChart);
        currentsChart = (LineChart) findViewById(R.id.currentsChart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadFileList(Logger.getStorageDir());
    }

    private void initializeCharts() {
        ArrayList<Entry> batteryAVoltageEntries = new ArrayList<Entry>();
        ArrayList<Entry> batteryBVoltageEntries = new ArrayList<Entry>();

        LineDataSet batteryAVoltages = new LineDataSet(batteryAVoltageEntries, "Battery A");
        LineDataSet batteryBVoltages = new LineDataSet(batteryAVoltageEntries, "Battery B");

        LineData batteryVoltageData = new LineData();
        batteryVoltageData.addDataSet(batteryAVoltages);
        batteryVoltageData.addDataSet(batteryBVoltages);

        batteryVoltagesChart.setData(batteryVoltageData);

        ArrayList<Entry> solarPanelCurrentEntries = new ArrayList<Entry>();
        ArrayList<Entry> motorCurrentEntries = new ArrayList<Entry>();

        LineDataSet solarPanelCurrents = new LineDataSet(solarPanelCurrentEntries, "Solar Panel");
        LineDataSet motorCurents = new LineDataSet(motorCurrentEntries, "Motor Current");

        LineData currentData = new LineData();
        currentData.addDataSet(solarPanelCurrents);
        currentData.addDataSet(motorCurents);

        currentsChart.setData(currentData);
    }

    public void onOpenLogFileButtonClicked(View view) {
    }

    public void openReplay(File file) {

    }
}
