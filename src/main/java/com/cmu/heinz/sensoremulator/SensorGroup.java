package com.cmu.heinz.sensoremulator;

import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
/**
 * @version 1.0
 * @author Owen Wagoner - Carnegie Mellon University - Heinz College
 */
public class SensorGroup {

    private List<Sensor> sensors = new ArrayList<>();
    private List<Sensor> runningSensors = new ArrayList<>();
    private List<Sensor> stoppedSensors = new ArrayList<>();
    private ServiceQueue sq = new ServiceQueue();
    
    private final String addSensorQueue = "AddSensorQueue";
    private final String deleteSensorQueue = "DeleteSensorQueue";
    
    /**
     * Adds a new sensor to stoppedSensors list
     * @param sensor - Sensor object to add
     */
    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
        stoppedSensors.add(sensor);
        
        BrokeredMessage m1 = new BrokeredMessage(sensor.getAddSensorMessage().serialize());
        m1.setDate(new Date());

        boolean r1 = sq.sendMessage(m1, addSensorQueue);
        if (r1) {
            System.out.println("Sensor successfully added.");
        } else {
            System.out.println("Error sending message to queue.");
            //Remove because failed to send message
            sensors.remove(sensor);
            stoppedSensors.remove(sensor);
        }
    }

    /**
     * Lists sensors in sensor list.
     */
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

    /**
     * Lists sensors in running sensor list.
     */
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
    
    /**
     * Lists sensors in stopped sensor list.
     */
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
    
    /**
     * Deletes a sensor from sensor group.
     * @param deleteSensorNum - index number of sensor to delete.
     */
    public void deleteSensor(int deleteSensorNum) {
        Sensor sensorToRemove = sensors.get(deleteSensorNum - 1);

        BrokeredMessage message = new BrokeredMessage(sensorToRemove.getAddSensorMessage().serialize());
        message.setDate(new Date());
        boolean result  = sq.sendMessage(message, deleteSensorQueue);
        if(result){
            sensorToRemove.stopSensor();
            stoppedSensors.remove(sensorToRemove);
            runningSensors.remove(sensorToRemove);
            sensors.remove(sensorToRemove);
            System.out.println("Sensor sucessfully removed.");
        }else{
            System.out.println("Message failed to send. Sensor was NOT removed.");
        }
        

    }

    /**
     * Method starts sensor with the provided index value.
     * @param sensorToRun - index value of sensor to start.
     * @param executor - executor to run sensor.
     */
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

    /**
     * Stops the sensor with the given index. 
     * @param sensorToStop - index of sensor to stop.
     * @param executor - executor on which sensor is running.
     */
    public void stopSensor(int sensorToStop, ExecutorService executor) {
        Sensor sensor = runningSensors.get(sensorToStop - 1);
        sensor.stopSensor();
        runningSensors.remove(sensor);
        stoppedSensors.add(sensor);
        System.out.println("Sensor stopped successfully.");
    }
    
}
