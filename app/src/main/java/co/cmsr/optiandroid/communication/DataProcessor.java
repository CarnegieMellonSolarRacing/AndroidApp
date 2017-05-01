package co.cmsr.optiandroid.communication;

import java.util.ArrayList;
import java.util.List;

import co.cmsr.optiandroid.datastructures.BoatConfig;
import co.cmsr.optiandroid.datastructures.BoatData;
import co.cmsr.optiandroid.datastructures.DataPacket;
import co.cmsr.optiandroid.datastructures.DataProcessorConfig;

/**
 * Created by jonbuckley on 4/27/17.
 */

public class DataProcessor {
    private int numCurrents;
    private int calibrationWindowSize;
    private int numPacketsReceived;
    private double[] currentSlopes;
    private double[] currentIntercepts;
    private double[][] currentCalibrationWindows;
    private boolean isCalibrated;

    public DataProcessor(BoatConfig boatConfig, DataProcessorConfig dpConfig) {
        this.numCurrents = boatConfig.numCurrents;
        this.calibrationWindowSize = dpConfig.calibrationWindowSize;
        this.currentSlopes = dpConfig.currentSlopes;

        this.currentIntercepts = new double[numCurrents];
        this.isCalibrated = false;
        numPacketsReceived = 0;

        currentCalibrationWindows = new double[numCurrents][calibrationWindowSize];
    }

    private void calibrateCurrents(List<Double> currents) {
        int bound = Math.min(currents.size(), currentCalibrationWindows.length);
        for (int i = 0; i < bound; i ++) {
            currentCalibrationWindows[i][numPacketsReceived] = currents.get(i).doubleValue();
        }
    }

    private double average(double[] values) {
        double sum = 0.0;
        for (double val : values) {
            sum += val;
        }

        return sum / (values.length > 0 ? values.length : 1);
    }

    private void determineCurrentNormalization() {
        for (int i = 0; i < currentCalibrationWindows.length; i ++) {
            currentIntercepts[i] = average(currentCalibrationWindows[i]);
        }

        isCalibrated = true;
    }

    private void normalizeCurrents(DataPacket dp) {
        ArrayList<Double> normalized = new ArrayList<>();
        int bound = Math.min(dp.currents.size(), currentIntercepts.length);
        for (int i = 0; i < bound; i ++) {
            double originalValue = dp.currents.get(i).doubleValue();
            normalized.add(new Double((originalValue - currentIntercepts[i]) * currentSlopes[i]));
        }

        dp.currents = normalized;
    }

    public boolean isCalibrated() {
        return isCalibrated;
    }

    public void onDataPacketReceievd(DataPacket dp) {
        if (numPacketsReceived < calibrationWindowSize) {
            calibrateCurrents(dp.currents);
            if (numPacketsReceived == calibrationWindowSize - 1) {
                determineCurrentNormalization();
            }
        } else {
            normalizeCurrents(dp);
        }

        numPacketsReceived ++;
    }
}
