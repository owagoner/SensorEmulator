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

    private final String sensorId;
    private final String sensorModel;
    private final String sensorManufacturer;
    private final String sensorHash;
    private final double sendFrequencyMin;
    private final int dataValueMod;
    private final BigInteger d;
    private final BigInteger n;
    private final BigInteger e = new BigInteger("65537");
    
    private boolean isCancelled = false;
    private ServiceQueue sq = new ServiceQueue();
    private final String sensorReadingQueue = "SensorReadingQueue";

    /**
     *
     * @param sensorId - Serial number or other identification number of sensor.
     * @param sensorModel - Model type of sensor.
     * @param sensorManufacturer - Manufacturer of sensor.
     * @param sendFrequencyMin - Frequency in minutes for which the sensor will
     *                          send data to message queue.
     * @param dataValueMod - Value to modulate the random 
     * @param d - RSA d value for sensor
     * @param n - RSA n value for sensor
     */
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
                //Stops the sensor run method.
                if (isCancelled) {
                    return;
                }
                //Generate random reading
                double currentDataReading = dataValueMod + rand.nextDouble() * 4 - 2;
                //Generate SRM object
                SensorReadingMessage srm
                        = new SensorReadingMessage(
                                sensorHash, 
                                Double.toString(currentDataReading), 
                                new Date()
                        );
                //Serialize object
                String srmStr = srm.serialize();
                //Create new brokered message
                BrokeredMessage message = new BrokeredMessage(srmStr);
                message.setDate(new Date());
                //Sets the value of the message signatures
                String signature = getDataSignature(srmStr);
                message.setProperty("Signature", signature);
                //Send message
                sq.sendMessage(message, sensorReadingQueue);
                System.out.println("Message sent: " + srmStr);
                //Sleep thread for specficied time 
                Thread.sleep(delayTime);
            }
        } catch (InterruptedException e) {
            System.out.println("Finished.");
        }
    }

    /**
     * Generates the data signature of the message
     * @param message - message value to sign
     * @return string value of the signed message.
     */
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

    /**
     * Stops sensor instance.
     */
    public void stopSensor() {
        isCancelled = true;
    }

    /**
     * Gets the AddSensorMessage for particular sensor.
     * @return AddSensorMessage
     */
    public AddSensorMessage getAddSensorMessage() {
        return new AddSensorMessage(this.sensorId, this.sensorModel, this.sensorManufacturer, this.sensorHash);
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

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
