package co.cmsr.optiandroid.communication;

import android.util.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;

import co.cmsr.optiandroid.datastructures.DataPacket;


public class DataParser {
    String buffer;

    static final String new_line_delim = "\n";
    static final String csv_delim = ",";

    public DataParser() {
        buffer = "";
    }

    public boolean  parseData(byte[] data, boolean isFirstInput, LinkedList<DataPacket> dataPackets,
                              double charge_left) {
        buffer += new String(data);

        // Try to parse data if we have received a "full line"
        if (buffer.contains("\n")) {

            if (!isFirstInput) {
                String[] lines = buffer.split(new_line_delim);

                String firstLine = lines[0];
                try {
                    String[] parts = firstLine.split(csv_delim);
                    float[] measurements = new float[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        measurements[i] = Float.parseFloat(parts[i]);
                    }
                    if (parts.length == 3) {
                        double batt_temp = measurements[0];
                        double batt_current_discharge = measurements[1];
                        double batt_current_charge = measurements[2];

                        // Calculate new charge left
                        charge_left = calc_leftover_charge(charge_left,
                                batt_current_discharge, batt_current_charge);
                        double percent_left = 100 * (
                                charge_left / dataPackets.getFirst().charge_left);
                        DataPacket new_dp = new DataPacket(batt_temp, charge_left,
                                percent_left, measurements[1], measurements[2]);
                        dataPackets.add(new_dp);
                    }
                } catch (Exception e) {
                    System.out.println("Could not parse the following line: " + firstLine);
                }
            } else isFirstInput = false;

            // Pop off the first line from the buffer.
            buffer = buffer.substring(buffer.length());
        }
        return isFirstInput;
    }

    public double calc_leftover_charge(double charge_left, double discharge_rate, double charge_rate) {
        double net_change = charge_rate - discharge_rate;
        return charge_left + net_change * ;
    }

//    public DataPacket getDataPacket() {
//        if (!parsedPackets.isEmpty()) {
//            return parsedPackets.removeFirst();
//        }
//
//        return null;
//    }

}
