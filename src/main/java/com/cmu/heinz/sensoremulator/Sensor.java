package com.cmu.heinz.sensoremulator;

import com.cmu.heinz.sensormessage.AddSensorMessage;
import com.cmu.heinz.sensormessage.SensorReadingMessage;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private BigInteger d;
    private BigInteger n;
    private BigInteger e = new BigInteger("65537");
    private boolean isCancelled = false;
    private ServiceQueue sq = new ServiceQueue();

    public Sensor(String sensorId, String sensorModel, String sensorManufacturer,
            double sendFrequencyMin, int dataValueMod, String d, String n) {
        this.sensorId = sensorId;
        this.sensorModel = sensorModel;
        this.sensorManufacturer = sensorManufacturer;
        this.sensorHash = getMetadataHash(sensorId + sensorModel + sensorManufacturer);
        this.sendFrequencyMin = sendFrequencyMin;
        this.dataValueMod = dataValueMod;
        this.d = new BigInteger(d);
        this.n = new BigInteger(n);
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
                //Generate random reading
                double currentDataReading = dataValueMod + rand.nextDouble() * 4 - 2;

                //Generate SRM object
                SensorReadingMessage srm
                        = new SensorReadingMessage(sensorHash, Double.toString(currentDataReading), new Date());
                //Serialize object
                String srmStr = srm.serialize();
                
                //send brokeredmessage
                BrokeredMessage message = new BrokeredMessage(srmStr);
                message.setDate(new Date());
                
                String signature = getDataSignature(srmStr);
                message.setProperty("Signature", signature);
                sq.sendSensorReadingMessage(message);

                System.out.println("Message sent: " + srmStr);
                Thread.sleep(delayTime);
            }
        } catch (InterruptedException e) {
            System.out.println("Finished.");
        }
    }

    public String getDataSignature(String message){
        System.out.println("Message||" + message + "||end message");
        String hashedMessage = getMetadataHash(message);
                
        BigInteger hashMessageBigInt = new BigInteger(hashedMessage.getBytes());
        
        BigInteger signature = hashMessageBigInt.modPow(d, n);
        
        return signature.toString();
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

    private String getMetadataHash(String str) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return bytesToHex(digest.digest(str.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error getting digest");
            return null;
        }
    }

    private final char[] hexArray = "0123456789abcdef".toCharArray();

    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
