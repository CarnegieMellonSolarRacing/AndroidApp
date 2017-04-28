package co.cmsr.optiandroid;

import android.provider.ContactsContract;
import android.util.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by jonbuckley on 4/9/17.
 */

public class DataParser {
    LinkedList<DataPacket> parsedPackets;
    String buffer;

    static final String deliminator = "\r\n";

    public DataParser() {
        buffer = "";
        parsedPackets = new LinkedList<DataPacket>();
    }

    public void onDataReceived(byte[] data) {
        buffer += new String(data);

        if (buffer.contains("\n")) {
            // Try to parse data if we have received a "full line"
            String[] lines = buffer.split(deliminator);

            String firstLine = lines[0];
            try {
                DataPacket packet = parse(new ByteArrayInputStream(firstLine.getBytes()));
                parsedPackets.add(packet);
            } catch (Exception e) {
                System.out.println("Could not parse the following line: " + firstLine);
            }

            // Pop off the first line from the buffer.
            buffer = buffer.substring(firstLine.length() + deliminator.length());
        }
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
