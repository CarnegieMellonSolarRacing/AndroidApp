package co.cmsr.optiandroid.datastructures;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jonbuckley on 4/9/17.
 */

public class DataPacket {
    public Double battery_temp;
    public Double charge_left;
    public Double battery_charge_rate;
    public Double battery_charge_percent;
    public Double solar_charge_rate;
    public Double boatSpeed;
    public Date time;

    public DataPacket(double battery_temp, double charge_left, double battery_charge_percent,
                      double battery_charge_rate, double solar_charge_rate) {
        this.battery_temp = battery_temp;
        this.charge_left = charge_left;
        this.battery_charge_rate = battery_charge_rate;
        this.solar_charge_rate = solar_charge_rate;
        this.battery_charge_percent = battery_charge_percent;
        this.boatSpeed = (double)0;
        this.time = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        return "Temperature: " + battery_temp.toString() +
                "Battery Charge: " + battery_charge_rate.toString() +
                "Solar Charge: " + solar_charge_rate.toString() +
                "Time: " + time.toString();
    }
}
