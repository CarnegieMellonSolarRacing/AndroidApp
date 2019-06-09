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
    double secToHour = 1.0 / 3600.0;

    // CSLA2DE
    int CSLA2DE_zero_v = 6;
    Double CSLA2DE_v_per_amp = .0256;
    int CSLA2DE_max_amps = 92;  // Amps reading
    Double CSLA2DE_max_v = CSLA2DE_max_amps * CSLA2DE_v_per_amp;
    Double CSLA2DE_max_analog = ((CSLA2DE_zero_v - CSLA2DE_max_v) / 5) * 1023;
    Double CSLA2DE_zero_analog = (CSLA2DE_zero_v / 5) * 1023.0;
    Double CSLA2DE_amps_per_step = CSLA2DE_max_amps / (CSLA2DE_zero_analog - CSLA2DE_max_analog);

    // CSLA1DK
    int A1D_zero_v = 6;
    Double A1D_v_per_amp = .0091;
    int A1D_max_amps = 325;  // Amps reading
    Double A1D_max_v = A1D_max_amps * A1D_v_per_amp;
    Double A1D_max_analog = ((A1D_zero_v - A1D_max_v) / 5) * 1023;
    Double A1D_zero_analog = (A1D_zero_v / 5) * 1023.0;
    Double A1D_amps_per_step = A1D_max_amps / (A1D_zero_analog - A1D_max_analog);


    public DataParser() {
        buffer = "";
    }

    public boolean  parseData(byte[] data, boolean isFirstInput, LinkedList<DataPacket> dataPackets,
                              float elapsedTime) {
        buffer += new String(data);

        // Try to parse data if we have received a complete message from arduino
        if (buffer.contains("\n")) {

            if (!isFirstInput) {
                Double charge_left = dataPackets.getLast().charge_left;
                String[] lines = buffer.split(new_line_delim);

                String firstLine = lines[0];
                try {
                    String[] parts = firstLine.split(csv_delim);
                    float[] measurements = new float[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        measurements[i] = Float.parseFloat(parts[i]);
                    }
                    if (parts.length == 3) {
                        double batt_temp = analogToTemp(measurements[0], false);
                        double batt_current_discharge = analogToCurrent(measurements[1], true);
                        double batt_current_charge = analogToCurrent(measurements[2], false);  // solar charging current

                        // Calculate new charge left
                        charge_left = calc_leftover_charge(charge_left,
                                batt_current_discharge, batt_current_charge, elapsedTime);
                        double percent_left = 100 * (
                                charge_left / dataPackets.getFirst().charge_left);
                        DataPacket new_dp = new DataPacket(batt_temp, charge_left,
                                percent_left, batt_current_discharge, batt_current_charge);
                        dataPackets.add(new_dp);
                    }
                } catch (Exception e) {
                    System.out.println("Could not parse the following line: " + firstLine);
                }
            } else isFirstInput = false;  // ignore first message since possibly truncated

            // Pop off the first line from the buffer.
            buffer = buffer.substring(buffer.length());
        }
        return isFirstInput;
    }

    public double calc_leftover_charge(Double charge_left,
                                       double discharge_rate,
                                       double charge_rate,
                                       double elapsedTimeSec) {
        double net_change = (charge_rate - discharge_rate) * elapsedTimeSec * secToHour;
        return charge_left + net_change;
    }

    /**
     * Reading is an analog value from 0 to 1023, so find ratio by dividing by 1024. Multiply
     * by input voltage into the sensor of 5v.
     *
     * Change this function's voltage to temperature conversion based on temperature sensor used,
     * the one last used was TMP36.
     * @param reading
     * @return
     */
    public double analogToTemp(float reading, boolean useCelsius) {
        double voltage = reading * 5.0 / 1024;
        double temperatureC = (voltage - 0.5) * 100 ;  //converting from 10 mv per degree wit 500 mV offset
        //to degrees ((voltage - 500mV) times 100)
        double temperatureF = (temperatureC * 9.0 / 5.0) + 32.0;

        if (useCelsius) return temperatureC;
        else return temperatureF;

    }

    public double analogToCurrent(float reading, boolean isCSLA1DK) {
        // Arduino analog bounded from 0 to 5v
        if (isCSLA1DK) {
            // Assuming voltage input of 12v
            return A1D_amps_per_step * (A1D_zero_analog - reading);
        }
        else { // CSLA2DE
            return CSLA2DE_amps_per_step * (CSLA2DE_zero_analog - reading);
        }
    }
//    public DataPacket getDataPacket() {
//        if (!parsedPackets.isEmpty()) {
//            return parsedPackets.removeFirst();
//        }
//
//        return null;
//    }

}
