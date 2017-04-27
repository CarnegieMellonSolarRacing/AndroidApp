package co.cmsr.optiandroid;

import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by jonbuckley on 4/26/17.
 */

public class DataManager {
    static final int LINE_CHART_MAX_POINTS = 25;

    DataParser dataParser;
    ArduinoUsbBridge bridge;
    CurrentChart currentChartOne;

    Button connectButton;
    long startTime;


    public DataManager(Context context, Button connectButton, LineChart chartOne) {
         this.connectButton = connectButton;

        dataParser = new DataParser();
        startTime = System.currentTimeMillis();

        bridge = new ArduinoUsbBridge(this, context);

        // Set up line chart.
        currentChartOne = new CurrentChart(chartOne, "Current (1)", LINE_CHART_MAX_POINTS);
    }

    public void onConnectionOpened() {
        connectButton.setBackgroundColor(Color.GREEN);
        connectButton.setText("Disconnect from Arduino");
    }

    public void onConnectionClosed() {
        connectButton.setBackgroundColor(Color.RED);
        connectButton.setText("Connect to Arduino");
    }

    public void onConnectButtonClicked(View view) {
        bridge.tryConnect();
    }

    public void onReceivedData(byte[] arg0) {
        dataParser.onDataReceived(arg0);
        DataPacket dp = dataParser.getDataPacket();

        onParsedPacket(dp);
    }

    public void onParsedPacket(DataPacket dp) {
        if (dp != null) {
            float currentTime = elapsedTime();

            // New datapacket received!
            currentChartOne.addPoint(currentTime, dp.currents.get(0).floatValue());
        }
    }

    private float elapsedTime() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;

        return (float) delta / 1000.0f;
    }
}
