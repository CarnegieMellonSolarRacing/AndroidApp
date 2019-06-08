package co.cmsr.optiandroid;

import android.app.Activity;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

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

/*
 * Data Manager is the high level interface between external events (ex: user click button
 * or arduino sends data) and any backend processing.
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
    LinkedList<DataPacket> dataPackets;

    // Battery Charge Calculations
    Double total_charge;
    Double initial_charge_percent;

    Button connectButton;
    long startTime;
    String logName;
    boolean saveLog;
    boolean isFirstInput;

    public DataManager(
            Context context,
            String trialName,
            boolean saveLog,
            Double initial_charge,
            Double initial_charge_percent,
            DataRenderer dataRenderer,
            BoatConfig boatConfig,
            BoatMap boatMap,
            DataProcessorConfig dpConfig) {
        this.context = context;
        this.total_charge = initial_charge;  // total charge left
        this.initial_charge_percent = initial_charge_percent;
        this.dataRenderer = dataRenderer;  // handles displaying data
        this.boatConfig = boatConfig;
        this.boatMap = boatMap;
        this.dataPackets = new LinkedList<DataPacket>();

        // First data value with dummy measurements and initial charge
        // Need to do this since every new charge estimate depends on previous in the linked list
        DataPacket new_dp = new DataPacket(0, initial_charge,
                initial_charge_percent, 0, 0);
        this.dataPackets.add(new_dp);

        // Get date and append to trial name for log name.
        Date today = new Date();
        String dateString = new SimpleDateFormat("dd-MM-yyyy").format(today);
        this.logName = String.format("%s-%s.log", trialName, dateString);
        this.saveLog = saveLog;
        this.isFirstInput = true;

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
        connectButton.setText("Connected!");
        dataRenderer.printDebug(total_charge.toString());
        isFirstInput = true;
        if (saveLog) {
            LoggerPacket lp = new LoggerPacket("CONNECTED", elapsedTime(), null);
            Logger.writeToFile(logName, lp.toJsonString() + "\n");
        }
    }

    public void onConnectionClosed() {
        connectButton.setBackgroundColor(Color.RED);
        connectButton.setText("Disconnected!");
        if (saveLog) {
            LoggerPacket lp = new LoggerPacket("DISCONNECTED", elapsedTime(), null);
            Logger.writeToFile(logName, lp.toJsonString() + "\n");
        }
    }

    public void onConnectButtonClicked(View view) {
        if (!bridge.connected) {
            if (!bridge.tryConnect()) {
              connectButton.setBackgroundColor(Color.RED);
              connectButton.setText("No USB Device Detected!");
            }
        } else {
            bridge.closeConnection();
        }
    }

    public void onReceivedData(byte[] received_bytes) {
        isFirstInput = dataParser.parseData(received_bytes, isFirstInput, dataPackets, total_charge);
        dataRenderer.renderData(dataPackets);
    }


    private float elapsedTime() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;

        return (float) delta / 1000.0f;
    }
}
