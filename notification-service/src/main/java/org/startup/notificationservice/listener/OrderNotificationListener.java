package org.startup.notificationservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationListener {

    @RabbitListener(queues = "order.queue")
    public void handleOrderCreated(String message) {
        System.out.println(">>> [Notification Service] Yeni bildiriş: " + message);

    }

}
