package co.cmsr.optiandroid.datastructures;

/**
 * Created by jonbuckley on 4/30/17.
 */

public class DataProcessorConfig {
    // In order of reception. Ie., first value, second value, third value.
    static final double CURRENT_SENSOR_A0 = 1.0/1.37;
    static final double CURRENT_SENSOR_A1 = 1.0/4.57;
    static final double CURRENT_SENSOR_A2 = 1.0/4.51;
    // static final double CURRENT_SENSOR_A3 = 1.0/4.405;
    // static final double CURRENT_SENSOR_A4 = 1.0/4.34;
    // static final double CURRENT_SENSOR_A5 = 1.0/2.04;

    static final double[] CURRENT_SLOPES = { CURRENT_SENSOR_A0, CURRENT_SENSOR_A1, CURRENT_SENSOR_A2 };
    static final int CALIBRATION_WINDOW_SIZE = 5;


    static final double VOLTAGE_SENSOR_0 = 4.348 * 5.0 / 1024.0;
    static final double VOLTAGE_SENSOR_1 = 4.405 * 5.0 / 1024.0;
    static final double VOLTAGE_SENSOR_2 = 11.38 * 5.0 / 1024.0 ;

    static final double[] VOLTAGE_COEFFICIENTS = { VOLTAGE_SENSOR_0, VOLTAGE_SENSOR_1, VOLTAGE_SENSOR_2 };

    public double[] currentSlopes;
    public double[] voltageCoefficients;
    public int calibrationWindowSize;

    public DataProcessorConfig(boolean useDefaults) {
        if (useDefaults) {
            currentSlopes = CURRENT_SLOPES;
            calibrationWindowSize = CALIBRATION_WINDOW_SIZE;
            voltageCoefficients = VOLTAGE_COEFFICIENTS;
        }
    }
}
