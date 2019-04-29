package co.cmsr.optiandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.cmsr.optiandroid.datastructures.BoatConfig;
import co.cmsr.optiandroid.datastructures.BoatMap;
import co.cmsr.optiandroid.datastructures.DataPacket;
import co.cmsr.optiandroid.datastructures.DataProcessorConfig;
import co.cmsr.optiandroid.renderers.BoatDataRenderer;

public class MainActivity extends AppCompatActivity {
    Button connnectButton;
    TextView trialDisplay;
    LineChart currentLineChart;
    BarChart voltagesBarChart;

    boolean debugEnabled;

    volatile DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        boolean saveLog = i.getBooleanExtra("save_log", false);
        String name = i.getStringExtra("trial_name");
        Float initial_charge = i.getFloatExtra("initial_charge", 0);
        debugEnabled = i.getBooleanExtra("debug_enabled", false);

        setContentView(R.layout.activity_main);

        trialDisplay = (TextView) findViewById(R.id.trialDisplay);

        Initialize(name, saveLog, initial_charge);
    }

    @Override
    protected void onPause() {
        dataManager.pause();

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    protected void Initialize(final String trialName, boolean saveLog, Float initial_charge) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                trialDisplay.setText(trialName);
            }
        });

        BoatMap boatMap = new BoatMap(true /* use defaults */);
        BoatConfig boatConfig = new BoatConfig(true /* use defaults */);
        DataProcessorConfig dpConfig = new DataProcessorConfig(true /* use defaults */);
        BoatDataRenderer renderer = new BoatDataRenderer(this, boatConfig);
        dataManager = new DataManager(
                this,
                trialName,
                saveLog,
                initial_charge,
                renderer,
                boatConfig,
                boatMap,
                dpConfig);
    }
}
