package co.cmsr.optiandroid;

import java.util.List;

/**
 * Created by jonbuckley on 4/9/17.
 */

public class DataPacket {
    public List<Double> temps;
    public List<Double> currents;

    public DataPacket(List<Double> temps, List<Double> currents) {
        this.temps = temps;
        this.currents = currents;
    }

    @Override
    public String toString() {
        return "Temperatures: " + temps.toString() + "\nCurrents: " + currents.toString();
    }
}
