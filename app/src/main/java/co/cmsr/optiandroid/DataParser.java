package co.cmsr.optiandroid;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonbuckley on 4/9/17.
 */

public class DataParser {
    public DataParser() {
    }

    public DataPacket Parse(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        reader.setLenient(true);
        try {
            return readDataPacket(reader);
        } finally {
            reader.close();
        }
    }

    public DataPacket readDataPacket(JsonReader reader) throws IOException {
        List<Double> currents = null;
        List<Double> temps = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("currents")) {
                currents = readCurrents(reader);
            } else if (name.equals("temps")) {
                temps = readTemps(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new DataPacket(currents, temps);
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
