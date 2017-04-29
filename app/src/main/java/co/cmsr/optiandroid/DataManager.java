package co.cmsr.optiandroid;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.Date;

import co.cmsr.optiandroid.charts.DynamicBarChart;
import co.cmsr.optiandroid.charts.DynamicLineChart;
import co.cmsr.optiandroid.communication.ArduinoUsbBridge;
import co.cmsr.optiandroid.communication.DataParser;
import co.cmsr.optiandroid.datastructures.BoatData;
import co.cmsr.optiandroid.datastructures.DataPacket;
import co.cmsr.optiandroid.renderers.DataRenderer;

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

    DataRenderer dataRenderer;

    Button connectButton;
    long startTime;
    String logName;
    boolean saveLog;

    public DataManager(
            Context context,
            String trialName,
            boolean saveLog,
            Button connectButton,
            DataRenderer dataRenderer,
            LineChart chartOne,
            BarChart chartTwo) {
        this.connectButton = connectButton;
        this.dataRenderer = dataRenderer;

        Date today = new Date();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(today);
        this.logName = String.format("%s-%s", trialName, dateString);
        this.saveLog = saveLog;

        dataParser = new DataParser();
        startTime = System.currentTimeMillis();

        bridge = new ArduinoUsbBridge(this, context);

//        // Set up line chart.
//        dynamicLineChartOne = new DynamicLineChart(chartOne, "Currents", LINE_CHART_MAX_POINTS);
//        ArrayList<String> barChartLabels = new ArrayList<String>(Arrays.asList("A","B"));
//        dynamicBarChartOne = new DynamicBarChart(chartTwo, barChartLabels, "Voltages", VOLT_MIN, VOLT_MAX);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConnectButtonClicked(view);
            }
        });
    }

    public void onConnectionOpened() {
        connectButton.setBackgroundColor(Color.GREEN);
        connectButton.setText("Disconnect from Arduino");

        if (saveLog) {
            Logger.writeToFile(logName, String.format("CONNECTED %d\n", elapsedTime()));
        }
    }

    public void onConnectionClosed() {
        connectButton.setBackgroundColor(Color.RED);
        connectButton.setText("Connect to Arduino");

        if (saveLog) {
            Logger.writeToFile(logName, String.format("%f DISCONNECTED\n", elapsedTime()));
        }
    }

    public void onConnectButtonClicked(View view) {
        if (!bridge.connected) {
            bridge.tryConnect();
        } else {
            bridge.closeConnection();
        }
    }

    public void onReceivedData(byte[] arg0) {
        dataParser.onDataReceived(arg0);
        DataPacket dp = dataParser.getDataPacket();

        onParsedPacket(dp);
    }

    public void onParsedPacket(DataPacket dp) {
        if (dp != null) {
            float currentTime = elapsedTime();

            dataRenderer.onPacketParsed(currentTime, new BoatData(dp));

//            if (dp.currents.size() >= 2) {
//                // New data packet received!
//                System.out.println(dp.toString()+"\n");
//                dynamicLineChartOne.addPoint(currentTime, dp.currents.get(1).floatValue());
//            }
//            dynamicBarChartOne.updateValues(dp.voltages);
//
//            if (saveLog) {
//                Logger.writeToFile(logName, String.format("%f %s\n", currentTime, dp.toString()));
//            }
        }
    }

    private float elapsedTime() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;

        return (float) delta / 1000.0f;
    }
}
