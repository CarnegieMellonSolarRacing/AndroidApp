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
    private double[] voltageCoefficients;
    private boolean isCalibrated;

    public DataProcessor(BoatConfig boatConfig, DataProcessorConfig dpConfig) {
        this.numCurrents = boatConfig.numCurrents;
        this.calibrationWindowSize = dpConfig.calibrationWindowSize;
        this.currentSlopes = dpConfig.currentSlopes;
        this.voltageCoefficients = dpConfig.voltageCoefficients;

        this.currentIntercepts = new double[numCurrents];
        this.isCalibrated = false;
        numPacketsReceived = 0;

        currentCalibrationWindows = new double[numCurrents][calibrationWindowSize];
    }

    private double average(double[] values) {
        double sum = 0.0;
        for (double val : values) {
            sum += val;
        }

        return sum / (values.length > 0 ? values.length : 1);
    }
}
