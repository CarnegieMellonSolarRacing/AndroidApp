package co.cmsr.optiandroid.replay;

import android.os.Handler;
import android.os.Looper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.cmsr.optiandroid.logging.LoggerPacket;

/**
 * Created by jonbuckley on 5/2/17.
 */

public class LogReplayer {
    private List<LoggerPacket> loggerPackets;

    public LogReplayer() {
        loggerPackets = new ArrayList<>();
    }

    public boolean loadData(File logfile) {
        try {
            FileReader fileReader = new FileReader(logfile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Gson gson = new Gson();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                LoggerPacket lp = gson.fromJson(line, LoggerPacket.class);
                loggerPackets.add(lp);
            }

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File was not found in LogReplayer.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not read from log file in LogPlayer.");
        }

        return false;
    }

    public void visualize(final LineChart batteryVoltages, final LineChart currents) {
        final LineData batteryVoltagesData = batteryVoltages.getLineData();
        final LineData currentsData = currents.getLineData();
        for (LoggerPacket lp : loggerPackets) {
            if (lp.type.equals("UPDATE") && lp.boatData != null) {
                float time = lp.elapsedTime;
                // Add to battery voltages.
                batteryVoltagesData.addEntry(new Entry(time, (float) lp.boatData.batteryAVoltage), 0);
                batteryVoltagesData.addEntry(new Entry(time, (float) lp.boatData.batteryBVoltage), 1);
                // Add to currents.
                currentsData.addEntry(new Entry(time, (float) lp.boatData.solarPanelCurrent), 0);
                currentsData.addEntry(new Entry(time, (float) lp.boatData.motorCurrent), 1);
            }
        }

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                batteryVoltagesData.notifyDataChanged();
                batteryVoltages.notifyDataSetChanged();
                batteryVoltages.invalidate();

                currentsData.notifyDataChanged();
                currents.notifyDataSetChanged();
                currents.invalidate();
            }
        });
    }

}
