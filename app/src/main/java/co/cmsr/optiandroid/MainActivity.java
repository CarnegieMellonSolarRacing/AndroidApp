package co.cmsr.optiandroid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncAdapterType;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends Activity {
    Button connnectButton;
    TextView textView, dataOutput;
    EditText editText;
    LineChart currentLineChart;
    Random random;

    volatile DataManager dataManager;

    // Charts
    CurrentChart currentChartOne;

    public static final String TAG = "AndroidOpti";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connnectButton = (Button) findViewById(R.id.buttonStart);
        dataOutput = (TextView) findViewById(R.id.OutputDisplay);
        currentLineChart = (LineChart) findViewById(R.id.currentLineChart);

        Initialize();
    }

    protected void Initialize() {
        random = new Random();

        dataManager = new DataManager(
                this,
                connnectButton,
                currentLineChart);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    transmitMockPacket(dataManager);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println("Refreshing line chart");
//                            currentLineChart.invalidate();
//                        }
//                    });
                    System.out.println("transmitting mock packet!");

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

        DataPacket dp = new DataPacket(currents, temps);

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
