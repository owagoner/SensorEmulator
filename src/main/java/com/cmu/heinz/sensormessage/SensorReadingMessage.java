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
public class SensorReadingMessage {
    
    private String sensorHash;
    private String documentId;
    private String documentCollection;
    private String documentDatabase;

    public SensorReadingMessage(String sensorHash, String documentId, String documentCollection, String documentDatabase) {
        this.sensorHash = sensorHash;
        this.documentId = documentId;
        this.documentCollection = documentCollection;
        this.documentDatabase = documentDatabase;
    }
    
    
    
    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
