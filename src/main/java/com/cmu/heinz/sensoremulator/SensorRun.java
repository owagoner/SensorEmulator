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

    public static void main(String[] args) {

        boolean continueApp = true;
        SensorGroup sensorGroup = new SensorGroup();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        String d1 = "18697994691663568605117347791714389629586385593973693034464019624862001103585";
        //String n1 = "82999896918691092906636116379408422728441471283407980941020915590610595288267";
        String d2 = "18007035584053326660351945138982218248187083625511483628972889665392460719493";
        //String n2 = "81506118590517499091061912326367679904671847594545533733939859375708709493117";
        String d3 = "50817941141564922993969120117303112535700357799764648708743566853258884903433";
        //String n3 = "66937099961707172309431297862078064240346632184638271358518022074590870962651";
        String d4 = "63152101713205254783413699357108793039427996994969933829896617412326509283473";
        //String n4 = "75220807858280920045446977840987949719298617234036198391888961707075667824499";
        //String e = "65537";
        
        Sensor s = new Sensor("serial1", "model1", "manufacturer1", 3, 5, d1);
        Sensor s2 = new Sensor("serial2", "model2", "manufacturer1", 5, 5, d2);
        Sensor s3 = new Sensor("serial3", "model1", "manufacturer2", 4, 5, d3);
        Sensor s4 = new Sensor("serial4", "model1", "manufacturer2", 10, 5, d4);
        sensorGroup.addSensor(s);
        sensorGroup.addSensor(s2);
        sensorGroup.addSensor(s3);
        sensorGroup.addSensor(s4);
        
        sensorGroup.startSensor(1, executor);
        sensorGroup.startSensor(1, executor);
        sensorGroup.startSensor(1, executor);
        sensorGroup.startSensor(1, executor);
        while(continueApp){
            
        }
//        while (continueApp) {
//            int choice = printMenu();
//
//            switch (choice) {
//                case 1:
//                    sensorGroup.listSensors();
//                    break;
//                case 2:
//                    Sensor s = createNewSensor();
//                    sensorGroup.addSensor(s);
//                    break;
//                case 3:
//                    sensorGroup.listSensors();
//                    int deleteSensorNum = kbi.getInteger(true, 0, 1, 100, "Enter the number of the sensor you want to remove.");
//                    sensorGroup.deleteSensor(deleteSensorNum);
//                    break;
//                case 4:
//                    sensorGroup.listStoppedSensors();
//                    int sensorToRun = kbi.getInteger(true, 0, 1, 100, "Enter the number of sensor you wish to start sending data.");
//                    sensorGroup.startSensor(sensorToRun, executor);
//                    break;
//                case 5:
//                    sensorGroup.listRunningSensors();
//                    int sensorToStop = kbi.getInteger(true, 0, 1, 100, "Enter the number of sensor you wish to stop.");
//                    sensorGroup.stopSensor(sensorToStop, executor);
//                    break;
//                case 6:
//                    executor.shutdownNow();
//                    System.exit(0);
//                default:
//                    break;
//            }
//
//        }
    }

    
//    public static Sensor createNewSensor() {
//        String sensorId = kbi.getString("", "Enter sensor serial number.");
//        String sensorModel = kbi.getString("", "Enter sensor model.");
//        String sensorManufacturer = kbi.getString("", "Enter sensor manufacturer.");
//        double sendFrequencyMin = kbi.getDouble(true, 5, 0, 60, "Enter how often sensor sends reading (in minutes) information.");
//        int dataValueMod = kbi.getInteger(true, 5, 1, 15, "Enter integer to modulate sensor data (data values are randomly generated).");
//        Sensor s = new Sensor(sensorId, sensorModel, sensorManufacturer, sendFrequencyMin, dataValueMod);
//        return s;
//    }

    public static int printMenu() {
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
