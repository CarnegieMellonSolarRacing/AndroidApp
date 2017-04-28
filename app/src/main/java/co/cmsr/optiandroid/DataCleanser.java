package co.cmsr.optiandroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonbuckley on 4/27/17.
 */

public class DataCleanser {
    public static final double[] CURRENT_SLOPES = { 0.0, 0.0, 0.0 };
    public static double[] CURRENT_INTERCEPTS = { 0.0, 0.0, 0.0 };

    public static final double[] VOLTAGE_SLOPES = { 0.0, 0.0, 0.0 };
    public static double[] VOLTAGE_INTERCEPTS = { 0.0, 0.0, 0.0 };

    public static final double[] TEMPERATURE_SLOPES = { 0.0, 0.0 };
    public static double[] TEMPERATURE_INTERCEPTS = { 0.0, 0.0 };

    public static List<Double> CleanseCurrents(List<Double> currents) {
        ArrayList<Double> cleansedValues = new ArrayList<>();

        for (int i = 0; i < CURRENT_SLOPES.length; i ++ ){
            Double d = currents.get(i);
            cleansedValues.add(new Double(d*CURRENT_SLOPES[i] + CURRENT_INTERCEPTS[i]));
        }

        return cleansedValues;
    }
}
