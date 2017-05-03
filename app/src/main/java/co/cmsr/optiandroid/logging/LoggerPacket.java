package co.cmsr.optiandroid.logging;

import com.google.gson.Gson;

import co.cmsr.optiandroid.datastructures.BoatData;

/**
 * Created by jonbuckley on 5/2/17.
 */

public class LoggerPacket {
    public String type;
    public float elapsedTime;
    public BoatData boatData;

    private transient Gson gson;

    public LoggerPacket() {
        gson = new Gson();
    }

    public LoggerPacket(String type, float elapsedTime, BoatData boatData) {
        this();

        this.type = type;
        this.elapsedTime = elapsedTime;
        this.boatData = boatData;
    }

    public String toJsonString() {
        return gson.toJson(this);
    }
}
