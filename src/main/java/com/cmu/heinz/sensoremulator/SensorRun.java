package com.cmu.heinz.sensoremulator;

import com.cmu.heinz.resources.KeyBoardInput;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version 1.0
 * @author Owen Wagoner - Carnegie Mellon University - Heinz College
 */
public class SensorRun {

    private static KeyBoardInput kbi = new KeyBoardInput();
    
    private static String DATABASE_ID = "{Database Name Here}";
    private static final String END_POINT = "{Endpoint Here}";
    private static final String MASTER_KEY = "{Key Here}";
    
    public static void main(String[] args) {

        boolean continueApp = true;
        SensorGroup sensorGroup = new SensorGroup();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        while(continueApp){
            int choice = printMenu();
        
            switch (choice) {
                case 1:
                    sensorGroup.listSensors();
                    break;
                case 2:
                    Sensor s = createNewSensor();
                    sensorGroup.addSensor(s);
                    break;
                case 3:
                    sensorGroup.listSensors();
                    int deleteSensorNum = kbi.getInteger(true, 0, 1, 100, "Enter the number of the sensor you want to remove.");
                    sensorGroup.deleteSensor(deleteSensorNum);
                    break;
                case 4:
                    sensorGroup.listStoppedSensors();
                    int sensorToRun = kbi.getInteger(true, 0, 1, 100, "Enter the number of sensor you wish to start sending data.");
                    sensorGroup.startSensor(sensorToRun, executor);
                    break;
                case 5:
                    sensorGroup.listRunningSensors();
                    int sensorToStop = kbi.getInteger(true, 0, 1, 100, "Enter the number of sensor you wish to stop.");
                    sensorGroup.stopSensor(sensorToStop, executor);
                    break;
                case 6:
                    executor.shutdownNow();
                    System.exit(0);
                default:
                    break;
            }
            
        }
    }
    
    public static Sensor createNewSensor(){
        String sensorId = kbi.getString("", "Enter sensor serial number.");
        String sensorModel = kbi.getString("", "Enter sensor model.");
        String sensorManufacturer = kbi.getString("", "Enter sensor manufacturer.");
        double sendFrequencyMin = kbi.getDouble(true,5, 0, 60, "Enter how often sensor sends reading (in minutes) information.");
        int dataValueMod = kbi.getInteger(true, 5, 1, 15, "Enter integer to modulate sensor data (data values are randomly generated).");
        String collectionName = sensorId + sensorModel + sensorManufacturer;        
        Sensor s = new Sensor(sensorId, sensorModel, sensorManufacturer, sendFrequencyMin, dataValueMod, collectionName, DATABASE_ID, END_POINT, MASTER_KEY);                
        return s;
    }
    
    public static int printMenu(){
        System.out.println("");
        System.out.println("1. List sensors");
        System.out.println("2. Add sensor");
        System.out.println("3. Delete sensor");
        System.out.println("4. Run sensor");
        System.out.println("5. Stop sensor");
        System.out.println("6. Exit");
        return kbi.getInteger(true, 6, 1, 6, "");
    }
}
