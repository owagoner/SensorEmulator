package com.cmu.heinz.sensormessage;

import com.google.gson.Gson;

/**
 * @author owagoner
 */
public class AddSensorMessage {
    private final String sensorId;
    private final String sensorModel;
    private final String sensorManufacturer;
    private final String sensorHash;

    /**
     *
     * @param sensorId - sensor serial or other id.
     * @param sensorModel - sensor model.
     * @param sensorManufacturer - sensor manufacturer.
     * @param sensorHash - SHA256 hash of sensorId, sensorModel, and sensorHash.
     */
    public AddSensorMessage(String sensorId, String sensorModel, String sensorManufacturer, String sensorHash) {
        this.sensorId = sensorId;
        this.sensorModel = sensorModel;
        this.sensorManufacturer = sensorManufacturer;
        this.sensorHash = sensorHash;
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
