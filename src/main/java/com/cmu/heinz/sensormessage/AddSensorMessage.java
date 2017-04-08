/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmu.heinz.sensormessage;

import com.google.gson.Gson;

/**
 *
 * @author owagoner
 */
public class AddSensorMessage {
    private String sensorId;
    private String sensorModel;
    private String sensorManufacturer;
    private String sensorHash;

    public AddSensorMessage(String sensorId, String sensorModel, String sensorManufacturer, String sensorHash) {
        this.sensorId = sensorId;
        this.sensorModel = sensorModel;
        this.sensorManufacturer = sensorManufacturer;
        this.sensorHash = sensorHash;
    }
    
    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
