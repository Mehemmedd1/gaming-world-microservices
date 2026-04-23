package org.startup.notificationservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.startup.notificationservice.config.RabbitMQConfig;

@Component
public class OrderNotificationListener {

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderCreated(String message) {
        System.out.println(">>> [Notification Service] Yeni bildiriş: " + message);

    }

}
