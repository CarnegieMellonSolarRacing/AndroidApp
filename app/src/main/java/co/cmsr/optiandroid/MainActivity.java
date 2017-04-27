package co.cmsr.optiandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {
    Button connnectButton;
    TextView trialDisplay;
    LineChart currentLineChart;
    BarChart voltagesBarChart;
    Random random;

    volatile DataManager dataManager;

    public static final String TAG = "AndroidOpti";
    public static final boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        boolean saveLog = i.getBooleanExtra("save_log", false);
        String name = i.getStringExtra("trial_name");

        setContentView(R.layout.activity_main);

        connnectButton = (Button) findViewById(R.id.buttonStart);
        trialDisplay = (TextView) findViewById(R.id.trialDisplay);
        currentLineChart = (LineChart) findViewById(R.id.currentLineChart);
        voltagesBarChart = (BarChart) findViewById(R.id.voltagesBarChart);

        Initialize(name, saveLog);
    }

    protected void Initialize(final String trialName, boolean saveLog) {
        random = new Random();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                trialDisplay.setText(trialName);
            }
        });
        dataManager = new DataManager(
                this,
                trialName,
                saveLog,
                connnectButton,
                currentLineChart,
                voltagesBarChart);

        if (DEBUG) {
            mockDataSource();
        }
    }

    private void mockDataSource() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    transmitMockPacket(dataManager);

                    try {
                        // sleep for half a second.
                        Thread.sleep((long) 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(r).start();
    }

    private void transmitMockPacket(DataManager dm) {
        DataPacket dp = samplePacket();
        dm.onParsedPacket(dp);
    }

    private DataPacket samplePacket() {
        List<Double> currents = new ArrayList<Double>();
        currents.add(new Double(random.nextDouble()));
        currents.add(new Double(random.nextDouble()));

        List<Double> temps = new ArrayList<Double>();
        temps.add(new Double(random.nextDouble()));
        temps.add(new Double(random.nextDouble()));

        List<Double> voltages = new ArrayList<Double>();
        voltages.add(new Double(random.nextDouble() * 30));
        voltages.add(new Double(random.nextDouble() * 30));

        DataPacket dp = new DataPacket(currents, temps, voltages);

        return dp;
    }

//    private void tvAppend(TextView tv, CharSequence text) {
//        final TextView ftv = tv;
//        final CharSequence ftext = text;
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ftv.append(ftext);
//            }
//        });
//    }
}
