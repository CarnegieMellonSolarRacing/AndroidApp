package co.cmsr.optiandroid;

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import co.cmsr.optiandroid.communication.ArduinoUsbBridge;
import co.cmsr.optiandroid.communication.DataParser;
import co.cmsr.optiandroid.communication.DataProcessor;
import co.cmsr.optiandroid.datastructures.BoatConfig;
import co.cmsr.optiandroid.datastructures.BoatData;
import co.cmsr.optiandroid.datastructures.BoatMap;
import co.cmsr.optiandroid.datastructures.DataPacket;
import co.cmsr.optiandroid.datastructures.DataProcessorConfig;
import co.cmsr.optiandroid.datastructures.LocalDataGenerator;
import co.cmsr.optiandroid.datastructures.LocalDataPacket;
import co.cmsr.optiandroid.logging.LoggerPacket;
import co.cmsr.optiandroid.logging.Logger;
import co.cmsr.optiandroid.renderers.DataRenderer;

/**
 * Created by jonbuckley on 4/26/17.
 */

public class DataManager {
    Context context;

    BoatConfig boatConfig;
    BoatMap boatMap;

    ArduinoUsbBridge bridge;
    DataParser dataParser;
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
            DataRenderer dataRenderer,
            BoatConfig boatConfig,
            BoatMap boatMap,
            DataProcessorConfig dpConfig) {
        this.context = context;
        this.dataRenderer = dataRenderer;
        this.boatConfig = boatConfig;
        this.boatMap = boatMap;

        // Get date and append to trial name for log name.
        Date today = new Date();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(today);
        this.logName = String.format("%s-%s.log", trialName, dateString);
        this.saveLog = saveLog;

        // Create data pipeline: UsbBridge -> parser -> processor -> localDataGenerator -> renderer
        bridge = new ArduinoUsbBridge(this, context);
        dataParser = new DataParser();
        dataProcessor = new DataProcessor(boatConfig, dpConfig);
        localDataGenerator = new LocalDataGenerator(context);

        startTime = System.currentTimeMillis();

        connectButton = (Button) ((Activity) context).findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConnectButtonClicked(view);
            }
        });
    }

    public void pause() {
        if (bridge.connected) {
            bridge.closeConnection();
        }
    }

    public void onConnectionOpened() {
        connectButton.setBackgroundColor(Color.GREEN);
        connectButton.setText("Disconnect from Arduino");

        if (saveLog) {
            LoggerPacket lp = new LoggerPacket("CONNECTED", elapsedTime(), null);
            Logger.writeToFile(logName, lp.toJsonString() + "\n");
        }
    }

    public void onConnectionClosed() {
        connectButton.setBackgroundColor(Color.RED);
        connectButton.setText("Connect to Arduino");

        if (saveLog) {
            LoggerPacket lp = new LoggerPacket("DISCONNECTED", elapsedTime(), null);
            Logger.writeToFile(logName, lp.toJsonString() + "\n");
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
            BoatData boatData = BoatData.generateBoatData(
                    context,
                    boatMap,
                    dataProcessor.isCalibrated(),
                    dp /* Data Packet */,
                    ldp /* Local Data Packet */);
            dataRenderer.onPacketParsed(currentTime, boatData);

            String boatDataJson = new LoggerPacket("UPDATE", currentTime, boatData).toJsonString();
            if (saveLog) {
                Logger.writeToFile(logName, boatDataJson + "\n");
            }

            System.out.println("Received packet " + elapsedTime());
        }
    }

    private float elapsedTime() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;

        return (float) delta / 1000.0f;
    }
}
