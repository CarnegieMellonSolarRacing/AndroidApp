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
    LinkedList<DataPacket> dataPackets;

    // Battery Charge Calculations
    Double total_charge;

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
            DataRenderer dataRenderer,
            BoatConfig boatConfig,
            BoatMap boatMap,
            DataProcessorConfig dpConfig) {
        this.context = context;
        this.total_charge = initial_charge;
        this.dataRenderer = dataRenderer;
        this.boatConfig = boatConfig;
        this.boatMap = boatMap;
        this.dataPackets = new LinkedList<DataPacket>();

        // First data value with dummy measurements and initial charge
        DataPacket new_dp = new DataPacket(0, initial_charge,
                0, 0, 0);
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

    public void onReceivedData(byte[] arg0) {
        isFirstInput = dataParser.parseData(arg0, isFirstInput, dataPackets, total_charge);
        dataRenderer.renderData(dataPackets);
    }


    private float elapsedTime() {
        long currentTime = System.currentTimeMillis();
        long delta = currentTime - startTime;

        return (float) delta / 1000.0f;
    }
}
