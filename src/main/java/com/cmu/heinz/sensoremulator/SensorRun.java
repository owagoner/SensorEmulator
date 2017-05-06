package com.cmu.heinz.sensoremulator;

import com.cmu.heinz.resources.KeyBoardInput;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @author Owen Wagoner - Carnegie Mellon University - Heinz College
 */
public class SensorRun {

    private static KeyBoardInput kbi = new KeyBoardInput();

    /**
     * Main method. 
     * @param args - default args
     */
    public static void main(String[] args) {
        
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException ex) {
            System.out.println("Unable to start program.");
            System.out.println(ex.toString());
        }
        
        boolean isConnected = false;
        while (!isConnected) {
            try {
                URL url = new URL("https://www.azure.com");

                URLConnection connection = url.openConnection();
                connection.connect();
                isConnected = true;

            } catch (IOException e) {
                System.out.println("Not connected to the internet... trying again.");
                System.out.println(e.toString());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    System.out.println("Unable to pause.");
                }
            }
        }
        
        SensorGroup sensorGroup = new SensorGroup();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //Predefined RSA pub/pri keys generated from java program.
        String d1 = "4850847502301221596890921192122751888360846412399356139627610055471155794229716764765136430502038862165203107274910832233419454187223318552848792157437953";
        String n1 = "5730379479402919351732944629729781003415854777198467921027698911377719767952005400186685612718627415523832368152925990087518079443760965616544955613253473";
        String d2 = "9013040054256644806897305208960476568439065048577701243740420865383445581383780132079453119201405394225251842938248902125721483222750246954391005831194113";
        String n2 = "9948757954555399435932640955984079511996884208118788109258685972658192664462984319597393255334657608979950199925384065361204564301678906113885107518699563";
        String d3 = "1022486434370368682827464818374139319534076809363837866541527792869970305775527602369564045468125179729505755497005573190501602253309670118401822485277313";
        String n3 = "9154466318214597317822890956528137784741091783507901948023511879961645345575430967017254942469195391815985822833293365660519249735869766042520978116125319";
        String d4 = "903058191728480144143892031423736286613703634136383128668805163454326626326024101436281872063013721040504695490600776773879793007700087667387491903159809";
        String n4 = "6312257328424637714031383539187009920627377887200953615994825511658084909292890497646408273414285478809250241989576125362382196426144064760027511411267711";
        //Default exponent value used in RSA
        //String e = "65537";

        Sensor s = new Sensor("serial1", "model1", "manufacturer1", 1.5, 5, d1, n1);
        Sensor s2 = new Sensor("serial2", "model2", "manufacturer1", 2, 5, d2, n2);
        Sensor s3 = new Sensor("serial3", "model1", "manufacturer2", 1.5, 5, d3, n3);
        Sensor s4 = new Sensor("serial4", "model1", "manufacturer2", 2, 5, d4, n4);
        sensorGroup.addSensor(s);
        sensorGroup.addSensor(s2);
        sensorGroup.addSensor(s3);
        sensorGroup.addSensor(s4);

        sensorGroup.startSensor(1, executor);
        sensorGroup.startSensor(1, executor);
        sensorGroup.startSensor(1, executor);
        sensorGroup.startSensor(1, executor);

//Sensors hardcoded to facilitate the simplicity of the demo.
        
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

    /**
     * Creates a new Sensor object from provided user input.
     * @return Sensor object.
     */
    public static Sensor createNewSensor() {
            String sensorId = kbi.getString("", "Enter sensor serial number.");
            String sensorModel = kbi.getString("", "Enter sensor model.");
            String sensorManufacturer = kbi.getString("", "Enter sensor manufacturer.");
            double sendFrequencyMin = kbi.getDouble(true, 5, 0, 60, "Enter how often sensor sends reading (in minutes) information.");
            int dataValueMod = kbi.getInteger(true, 5, 1, 15, "Enter integer to modulate sensor data (data values are randomly generated).");
            String dVal = kbi.getString("", "Enter RSA D value.");
            String nVal = kbi.getString("", "Enter RSA N value.");
            Sensor s = new Sensor(sensorId, sensorModel, sensorManufacturer, sendFrequencyMin, dataValueMod, dVal, nVal);
            return s;
        }

    /**
     * Prints menu options and returns user choice.
     * @return int of choice by user.
     */
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
