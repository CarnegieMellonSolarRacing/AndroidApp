package co.cmsr.optiandroid.datastructures;

import java.util.List;

/**
 * Created by jonbuckley on 4/9/17.
 */

public class DataPacket {
    public List<Double> temps;
    public List<Double> currents;
    public List<Double> voltages;

    public DataPacket(List<Double> temps, List<Double> currents, List<Double> voltages) {
        this.temps = temps;
        this.currents = currents;
        this.voltages = voltages;
    }

    @Override
    public String toString() {
        return "Temperatures: " + temps.toString() +
                " Currents: " + currents.toString() +
                " Voltages" + voltages.toString();
    }
}
