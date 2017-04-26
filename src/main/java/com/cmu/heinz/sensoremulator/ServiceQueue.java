/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cmu.heinz.sensoremulator;

import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.microsoft.windowsazure.exception.ServiceException;

/**
 *
 * @author owagoner
 */
public class ServiceQueue {

    private ServiceBusContract service;
    private String url = "servicebus.windows.net/";
    private String namespace = "farmiot.";
    private String key1 = "mAxLq2xmPeZAWrHU6Q9QeWV7oFySuxoRBRsuU4VuTT8=";
    private String key2 = "bSboJeK2ZZpWn9Aur9W6m33VvGkfZBC0FZTchEeEmyA=";
    private String endpoint = "Endpoint=sb://farmiot.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=mAxLq2xmPeZAWrHU6Q9QeWV7oFySuxoRBRsuU4VuTT8=";
    private String endpoint2 = "Endpoint=sb://farmiot.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=bSboJeK2ZZpWn9Aur9W6m33VvGkfZBC0FZTchEeEmyA=";
    private String addSensorQueue = "AddSensorQueue";
    private String sensorReadingQueue = "SensorReadingQueue";
    private String deleteSensorQueue = "DeleteSensorQueue";
    
    public ServiceQueue() {
//        Configuration config
//                = ServiceBusConfiguration.configureWithSASAuthentication(
//                        "HowToSample",
//                        "RootManageSharedAccessKey",
//                        "SAS_key_value",
//                        ".servicebus.windows.net"
//                );
        Configuration config = ServiceBusConfiguration.configureWithSASAuthentication(namespace, "RootManageSharedAccessKey", key1, url);
        service = ServiceBusService.create(config);
    }

    public boolean sendAddSensorMessage(BrokeredMessage message) {
        try {
            service.sendQueueMessage(addSensorQueue, message);
            return true;
        } catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean sendSensorReadingMessage(BrokeredMessage message) {
        try {
            service.sendQueueMessage(sensorReadingQueue, message);
            return true;
        } catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public void getServiceInfo() {

        QueueInfo queueInfo = new QueueInfo("TestQueue");
        try {
            CreateQueueResult result = service.createQueue(queueInfo);
        } catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public boolean sendDeleteSensorMessage(BrokeredMessage message) {
        try {
            service.sendQueueMessage(deleteSensorQueue, message);
            return true;
        } catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            return false;
        }
    }

}
