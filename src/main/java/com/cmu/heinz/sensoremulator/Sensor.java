package com.cmu.heinz.sensoremulator;

import com.cmu.heinz.resources.RSA;
import com.cmu.heinz.sensormessage.AddSensorMessage;
import com.cmu.heinz.sensormessage.SensorReadingMessage;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import org.bouncycastle.jcajce.provider.digest.Keccak;

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
    private BigInteger e = new BigInteger("65537");
    private boolean isCancelled = false;
    private ServiceQueue sq = new ServiceQueue();

    public Sensor(String sensorId, String sensorModel, String sensorManufacturer,
            double sendFrequencyMin, int dataValueMod, String d) {
        this.sensorId = sensorId;
        this.sensorModel = sensorModel;
        this.sensorManufacturer = sensorManufacturer;
        this.sensorHash = getMetadataHash();
        this.sendFrequencyMin = sendFrequencyMin;
        this.dataValueMod = dataValueMod;
        this.d = new BigInteger(d);
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
                //Hash message
                String hashedMessage = getKeccak256Hash(srmStr);
                //New instance of simple rsa with sensor private key
                RSA rsa = new RSA(d, e);
                String encryptedHashedMessage = rsa.encrypt(hashedMessage);

                //send brokeredmessage
                BrokeredMessage message = new BrokeredMessage(srmStr);
                message.setDate(new Date());
                message.setProperty("Signature", encryptedHashedMessage);
                sq.sendSensorReadingMessage(message);
                
                System.out.println("Message sent: " + srmStr);
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

    private String getMetadataHash() {
        Keccak.Digest256 digest = new Keccak.Digest256();
        String str = this.sensorId + this.sensorModel + this.sensorManufacturer;
        digest.update(str.getBytes());
        return "0x" + bytesToHex(digest.digest());
    }

    private String getKeccak256Hash(String str) {
        Keccak.Digest256 digest = new Keccak.Digest256();
        digest.update(str.getBytes());
        return bytesToHex(digest.digest());
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
