package co.cmsr.optiandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jonbuckley on 4/26/17.
 */

public class DataManager {
    static final int LINE_CHART_MAX_POINTS = 25;
    static final float VOLT_MIN = 0.0f, VOLT_MAX = 30.0f;

    DataParser dataParser;
    ArduinoUsbBridge bridge;

    DynamicLineChart dynamicLineChartOne;
    DynamicBarChart dynamicBarChartOne;

    Button connectButton;
    long startTime;


    public DataManager(
            Context context,
            Button connectButton,
            LineChart chartOne,
            BarChart chartTwo) {
         this.connectButton = connectButton;

        dataParser = new DataParser();
        startTime = System.currentTimeMillis();

        bridge = new ArduinoUsbBridge(this, context);

        // Set up line chart.
        dynamicLineChartOne = new DynamicLineChart(chartOne, "Current (1)", LINE_CHART_MAX_POINTS);
        ArrayList<String> barChartLabels = new ArrayList<String>(Arrays.asList("A","B"));
        dynamicBarChartOne = new DynamicBarChart(chartTwo, barChartLabels, "Voltages", VOLT_MIN, VOLT_MAX);
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

            // New data packet received!
            dynamicLineChartOne.addPoint(currentTime, dp.currents.get(0).floatValue());
            dynamicBarChartOne.updateValues(dp.voltages);
        }
    }

    private float elapsedTime() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;

        return (float) delta / 1000.0f;
    }
}
