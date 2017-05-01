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
import co.cmsr.optiandroid.communication.DataProcessor;
import co.cmsr.optiandroid.datastructures.BoatData;
import co.cmsr.optiandroid.datastructures.BoatMap;
import co.cmsr.optiandroid.datastructures.DataPacket;
import co.cmsr.optiandroid.datastructures.LocalDataGenerator;
import co.cmsr.optiandroid.datastructures.LocalDataPacket;
import co.cmsr.optiandroid.renderers.DataRenderer;

/**
 * Created by jonbuckley on 4/26/17.
 */

public class DataManager {
    static final int LINE_CHART_MAX_POINTS = 25;
    static final float VOLT_MIN = 0.0f, VOLT_MAX = 17.0f;
    static final double[] CURRENT_SLOPES = { 1.0, 1.0, 1.0, 1.0 };
    static final int CALIBRATION_WINDOW_SIZE = 5;

    Context context;
    DataParser dataParser;
    ArduinoUsbBridge bridge;

    DynamicLineChart dynamicLineChartOne;
    DynamicBarChart dynamicBarChartOne;

    DataProcessor dataProcessor;
    LocalDataGenerator localDataGenerator;
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
        this.context = context;
        this.connectButton = connectButton;
        this.dataRenderer = dataRenderer;

        Date today = new Date();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(today);
        this.logName = String.format("%s-%s", trialName, dateString);
        this.saveLog = saveLog;

        dataParser = new DataParser();
        dataProcessor = new DataProcessor(
                BoatMap.NUM_CURRENTS,
                CALIBRATION_WINDOW_SIZE,
                CURRENT_SLOPES);
        localDataGenerator = new LocalDataGenerator(context);
        startTime = System.currentTimeMillis();

        bridge = new ArduinoUsbBridge(this, context);

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

            // This will modify dp to be cleansed.
            dataProcessor.onDataPacketReceievd(dp);
            // Gather local data.
            LocalDataPacket ldp = localDataGenerator.gatherLocalData();
            // Display the data.
            dataRenderer.onPacketParsed(currentTime, new BoatData(
                    context,
                    dataProcessor.isCalibrated(),
                    dp /* Data Packet */,
                    ldp) /* Local Data Packet */);
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
