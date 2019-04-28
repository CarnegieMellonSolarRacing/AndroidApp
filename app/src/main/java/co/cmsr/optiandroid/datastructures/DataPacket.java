package co.cmsr.optiandroid.datastructures;

import java.util.List;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jonbuckley on 4/9/17.
 */

public class DataPacket {
    public Double battery_temp;
    public Double battery_charge;
    public Double battery_charge_percent;
    public Double solar_charge;
    public Double boatSpeed;
    public Date time;

    public DataPacket(double battery_temp, double battery_charge, double solar_charge) {
        this.battery_temp = battery_temp;
        this.battery_charge = battery_charge;
        this.solar_charge = solar_charge;
        this.time = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return "Temperature: " + battery_temp.toString() +
                "Battery Charge: " + battery_charge.toString() +
                "Solar Charge: " + solar_charge.toString() +
                "Time: " + time.toString();
    }
}
