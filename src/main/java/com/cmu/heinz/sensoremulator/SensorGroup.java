/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmu.heinz.sensoremulator;

import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
/**
 *
 * @author owagoner
 */
public class SensorGroup {

    private List<Sensor> sensors = new ArrayList<>();
    private List<Sensor> runningSensors = new ArrayList<>();
    private List<Sensor> stoppedSensors = new ArrayList<>();
    private ServiceQueue sq = new ServiceQueue();

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
        stoppedSensors.add(sensor);
        
        BrokeredMessage m1 = new BrokeredMessage(sensor.getAddSensorMessage().serialize());
        m1.setDate(new Date());

        boolean r1 = sq.sendAddSensorMessage(m1);
        if (r1) {
            System.out.println("Sensor successfully added.");
        } else {
            System.out.println("Error sending message to queue.");
            //Remove because failed to send message
            sensors.remove(sensor);
            stoppedSensors.remove(sensor);
        }
    }

    public void listSensors() {
        int cnt = 1;
        if (sensors.size() < 1) {
            System.out.println("No sensors.");
            return;
        }
        for (Sensor s : sensors) {
            System.out.println(cnt + ". " + s.toString());
            cnt++;
        }
    }

    public void listRunningSensors() {
        int cnt = 1;
        if (runningSensors.size() < 1) {
            System.out.println("No sensors running.");
            return;
        }
        System.out.println("Running sensors");
        for (Sensor s : runningSensors) {
            System.out.println(cnt + ". " + s.toString());
            cnt++;
        }
    }
    
    public void listStoppedSensors(){
        int cnt = 1;
        if (stoppedSensors.size() < 1) {
            System.out.println("All sensors running.");
            return;
        }
        System.out.println("Stopped sensors");
        for (Sensor s : stoppedSensors) {
            System.out.println(cnt + ". " + s.toString());
            cnt++;
        }
    }
    
    public void deleteSensor(int deleteSensorNum) {
        Sensor sensorToRemove = sensors.get(deleteSensorNum - 1);

        BrokeredMessage message = new BrokeredMessage(sensorToRemove.getAddSensorMessage().serialize());
        message.setDate(new Date());
        boolean result  = sq.sendDeleteSensorMessage(message);
        if(true){
            sensorToRemove.stopSensor();
            stoppedSensors.remove(sensorToRemove);
            runningSensors.remove(sensorToRemove);
            sensors.remove(sensorToRemove);
            System.out.println("Sensor sucessfully removed.");
        }else{
            System.out.println("Message failed to send. Sensor was NOT removed.");
        }
        

    }

    public void startSensor(int sensorToRun, ExecutorService executor) {
        Sensor sensor = stoppedSensors.get(sensorToRun - 1);
        try {
            executor.execute(sensor);
            runningSensors.add(sensor);
            stoppedSensors.remove(sensor);
            System.out.println("Sensor started sucessfully.");
        } catch (Exception e) {
            System.out.println("Error starting sensor.");
        }

    }

    public void stopSensor(int sensorToStop, ExecutorService executor) {
        Sensor sensor = runningSensors.get(sensorToStop - 1);
        sensor.stopSensor();
        runningSensors.remove(sensor);
        stoppedSensors.add(sensor);
        System.out.println("Sensor stopped successfully.");
    }
    
}
