package com.pandurang.orders.web.exception.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
class RabbitMQListener {

    @RabbitListener(queues = "${orders.new-orders-queue}")
    public void handleNewOrder(RabbitMQDemoController.MyPayload payload) {
        System.out.println("New Order: "+payload.content());
    }

    @RabbitListener(queues = "${orders.delivered-orders-queue}")
    public void handleDeliveredOrder(RabbitMQDemoController.MyPayload payload) {
        System.out.println("Delivered Order: "+payload.content());
    }
}
