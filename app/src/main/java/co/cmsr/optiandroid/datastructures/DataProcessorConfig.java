package co.cmsr.optiandroid.datastructures;

/**
 * Created by jonbuckley on 4/30/17.
 */

public class DataProcessorConfig {
    static final double[] CURRENT_SLOPES = { 1.0, 1.0, 1.0 };
    static final int CALIBRATION_WINDOW_SIZE = 5;

    public double[] currentSlopes;
    public int calibrationWindowSize;

    public DataProcessorConfig(boolean useDefaults) {
        if (useDefaults) {
            currentSlopes = CURRENT_SLOPES;
            calibrationWindowSize = CALIBRATION_WINDOW_SIZE;
        }
    }
}
