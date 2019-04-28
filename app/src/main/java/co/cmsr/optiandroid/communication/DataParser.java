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
import co.cmsr.optiandroid.renderers.DataRenderer;

/**
 * Created by jonbuckley on 4/9/17.
 */

//public class ExceptionHandler implements
//        java.lang.Thread.UncaughtExceptionHandler {
//    private final Activity myContext;
//    private final String LINE_SEPARATOR = "\n";
//
//    public ExceptionHandler(Activity context) {
//        myContext = context;
//
//    }
//    public void uncaughtException(Thread thread, Throwable exception) {
//
//    }
//}

public class DataParser {
    LinkedList<DataPacket> parsedPackets;
    String buffer;

    static final String deliminator = "\n";

    public DataParser() {
        buffer = "";
        parsedPackets = new LinkedList<DataPacket>();
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    public boolean parseData(byte[] data, DataRenderer renderer, boolean isFirstInput) {
        buffer += new String(data);

        // Try to parse data if we have received a "full line"
        if (buffer.contains("\n")) {

            if (!isFirstInput) {
                String[] lines = buffer.split(deliminator);
                String firstLine = lines[0];
                try {
                    String[] parts = firstLine.split(",");
                    int[] ints = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        ints[i] = Integer.parseInt(parts[i]);
                        renderer.printDebug(parts[i]);
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

    public DataPacket getDataPacket() {
        if (!parsedPackets.isEmpty()) {
            return parsedPackets.removeFirst();
        }

        return null;
    }

    public DataPacket parse(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        try {
            return readDataPacket(reader);
        } finally {
            reader.close();
        }
    }

    public DataPacket readDataPacket(JsonReader reader) throws IOException {
        List<Double> currents = new ArrayList<Double>();
        List<Double> temps = new ArrayList<Double>();
        List<Double> voltages = new ArrayList<Double>();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("currents")) {
                currents = readCurrents(reader);
            } else if (name.equals("temps")) {
                temps = readTemps(reader);
            } else if (name.equals("volts")) {
                voltages = readVoltages(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DataPacket(temps, currents, voltages);
    }

    private List<Double> readCurrents(JsonReader reader) throws IOException {
        List<Double> currents = new ArrayList<Double>();
        reader.beginArray();
        while (reader.hasNext()) {
            currents.add(reader.nextDouble());
        }
        reader.endArray();

        return currents;
    }

    private List<Double> readVoltages(JsonReader reader) throws IOException {
        List<Double> voltages = new ArrayList<Double>();
        reader.beginArray();
        while (reader.hasNext()) {
            voltages.add(reader.nextDouble());
        }
        reader.endArray();

        return voltages;
    }

    private List<Double> readTemps(JsonReader reader) throws IOException {
        List<Double> temps = new ArrayList<Double>();
        reader.beginArray();
        while (reader.hasNext()) {
            temps.add(reader.nextDouble());
        }
        reader.endArray();

        return temps;
    }

}
