package com.example.crud_cosmodb.messaging;

import com.azure.messaging.servicebus.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AzureServiceBusSender {

    private final ServiceBusSenderClient senderClient;

    public AzureServiceBusSender(
            @Value("${azure.servicebus.connection-string}") String connectionString,
            @Value("${azure.servicebus.queue-name}") String queueName) {

        this.senderClient = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
    }

    public void enviarMensaje(String mensaje) {
        senderClient.sendMessage(new ServiceBusMessage(mensaje));
    }
}
