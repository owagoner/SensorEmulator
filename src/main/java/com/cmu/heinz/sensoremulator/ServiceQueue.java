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

    private final ServiceBusContract service;
    private final String url = "servicebus.windows.net/";
    //REPLACE WITH YOUR ENDPOINT AND KEY. THESE RESOURCES HAVE BEEN DELETED.
    private final String namespace = "farmiot.";
    private final String key = "mAxLq2xmPeZAWrHU6Q9QeWV7oFySuxoRBRsuU4VuTT8=";
    /**
     * Default contractor that initializes the ServiceBusContract.
     */
    public ServiceQueue() {
        Configuration config = 
                ServiceBusConfiguration
                        .configureWithSASAuthentication(
                                namespace, 
                                "RootManageSharedAccessKey", 
                                key, 
                                url
                        );
        service = ServiceBusService.create(config);
    }

    /**
     * Sends provided brokered message to provided queue.
     * @param message - message to send.
     * @param queueName - queue to send message.
     * @return success of send
     */
    public boolean sendMessage(BrokeredMessage message, String queueName){
        try {
            service.sendQueueMessage(queueName, message);
            return true;
        } catch (ServiceException e) {
            System.out.print("ServiceException encountered: ");
            System.out.println(e.getMessage());
            return false;
        }
    }
}