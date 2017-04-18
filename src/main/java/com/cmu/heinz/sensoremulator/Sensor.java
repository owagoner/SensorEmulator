package com.cmu.heinz.sensoremulator;

import com.cmu.heinz.resources.DocumentDb;
import com.cmu.heinz.sensormessage.AddSensorMessage;
import com.cmu.heinz.sensormessage.SensorReadingMessage;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

/**
 * @version 1.0
 * @author Owen Wagoner - Carnegie Mellon University - Heinz College
 */
public class Sensor implements Runnable {

    private String sensorId;
    private String sensorModel;
    private String sensorManufacturer;
    private String sensorHash;
    private double sendFrequencyMin;
    private int dataValueMod;
    private String collectionName;
    private boolean isCancelled = false;
    private ServiceQueue sq = new ServiceQueue();
    private String DATABASE_ID;
    private String END_POINT;
    private String MASTER_KEY;
    
    public Sensor(String sensorId, String sensorModel, String sensorManufacturer, 
            double sendFrequencyMin, int dataValueMod, String collectionName, 
            String DATABASE_ID, String END_POINT, String MASTER_KEY) {
        this.sensorId = sensorId;
        this.sensorModel = sensorModel;
        this.sensorManufacturer = sensorManufacturer;
        this.sensorHash = getMetadataHash();
        this.sendFrequencyMin = sendFrequencyMin;
        this.dataValueMod = dataValueMod;
        this.collectionName = collectionName;
        this.DATABASE_ID = DATABASE_ID;
        this.END_POINT = END_POINT;
        this.MASTER_KEY = MASTER_KEY;
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();
            long delayTime = new Long((int) (sendFrequencyMin * 60 * 1000));

            while (true) {
                if (isCancelled) {
                    return;
                }
                double currentDataReading = dataValueMod + rand.nextDouble() * 4 - 2;
                
//                SensorDataPoint sdp = new SensorDataPoint(sensorId, sensorHash, currentDataReading, new Date());
//                DocumentDb ddb;
//                try {
//                    ddb = new DocumentDb(DATABASE_ID, END_POINT, MASTER_KEY);
//                    sdp = ddb.addSensorReading(collectionName, sdp);
//                    System.out.println("Stored in db: " + sdp.serialize());
//                } catch (DocumentClientException ex) {
//                    System.out.println("Unable to send data to database.");
//                }
                SensorReadingMessage srm = 
                        new SensorReadingMessage(sensorHash, Double.toString(currentDataReading), new Date(), DATABASE_ID);
                
                //send message
                BrokeredMessage message = new BrokeredMessage(srm.serialize());
                message.setDate(new Date());
                sq.sendSensorReadingMessage(message);
                System.out.println("Message sent: " + srm.serialize());
                Thread.sleep(delayTime);
            }
        } catch (InterruptedException e) {
            System.out.println("Finished.");
        }
    }

    @Override
    public String toString() {
        return "SensorId: " + this.sensorId + " SendFrequencyMin: "
                + this.sendFrequencyMin + " DataValueMod : " + this.dataValueMod;
    }

    public void stopSensor() {
        isCancelled = true;
    }

    public AddSensorMessage getAddSensorMessage() {
        return new AddSensorMessage(this.sensorId, this.sensorModel, this.sensorManufacturer, this.sensorHash);
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
    
    private String getMetadataHash(){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            String metadata = this.sensorId + this.sensorModel + this.sensorManufacturer;
            byte[] hash = digest.digest(metadata.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Unable to hash sensor metadata");
            return null;
        }
    }
}
