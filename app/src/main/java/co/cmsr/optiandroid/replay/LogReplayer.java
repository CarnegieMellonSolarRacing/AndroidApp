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

}
