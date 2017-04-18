/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmu.heinz.sensormessage;

import com.google.gson.Gson;
import java.util.Date;

/**
 *
 * @author owagoner
 */
public class SensorReadingMessage {
    
    private String sensorHash;
    private String data;
    private Date date;
    private String signature;

    public SensorReadingMessage(String sensorHash, String data, Date date, String signature) {
        this.sensorHash = sensorHash;
        this.data = data;
        this.date = date;
        this.signature = signature;
    }
    
    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}