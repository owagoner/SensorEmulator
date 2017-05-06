package com.cmu.heinz.sensormessage;

import com.google.gson.Gson;
import java.util.Date;

/**
 *
 * @author owagoner
 */
public class SensorReadingMessage {
    
    private final String sensorHash;
    private final String data;
    private final Date date;

    /**
     * Constructor that generates SensorReadingMessage with provided hash, data,
     *  and date values.
     * @param sensorHash - SHA256 hash of sensor metadata.
     * @param data - sensor data value.
     * @param date - date of data value.
     */
    public SensorReadingMessage(String sensorHash, String data, Date date) {
        this.sensorHash = sensorHash;
        this.data = data;
        this.date = date;
    }
    
    /**
     * JSON serialized string of object.
     * @return JSON string.
     */
    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
