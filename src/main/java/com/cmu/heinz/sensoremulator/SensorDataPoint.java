package com.cmu.heinz.sensoremulator;


import com.google.gson.Gson;
import java.util.Date;


/**
 * @version 1.0
 * @author Owen Wagoner - Carnegie Mellon University - Heinz College
 */
public class SensorDataPoint {

    private String sensorId;
    private String sensorHash;
    private double dataPoint;
    private Date date;
    private String id;

    public SensorDataPoint(String sensorId, String sensorHash, double dataPoint, Date date) {
        this.sensorId = sensorId;
        this.sensorHash = sensorHash;
        this.dataPoint = dataPoint;
        this.date = date;
    }

    public SensorDataPoint() {
    }

    public SensorDataPoint(String sensorId, String sensorHash, double dataPoint, Date date, String id) {
        this.sensorId = sensorId;
        this.sensorHash = sensorHash;
        this.dataPoint = dataPoint;
        this.date = date;
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorHash() {
        return sensorHash;
    }

    public void setSensorHash(String sensorHash) {
        this.sensorHash = sensorHash;
    }

    public double getDataPoint() {
        return dataPoint;
    }

    public void setDataPoint(double dataPoint) {
        this.dataPoint = dataPoint;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String serialize(){
        Gson gson = new Gson();
        
        return gson.toJson(this);
    }
    
}
