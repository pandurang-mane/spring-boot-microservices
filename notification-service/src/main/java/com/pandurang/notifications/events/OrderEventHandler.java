package com.pandurang.notifications.events;

import com.pandurang.notifications.domain.NotificationService;
import com.pandurang.notifications.domain.OrderEventEntity;
import com.pandurang.notifications.domain.OrderEventRepository;
import com.pandurang.notifications.domain.models.OrderCancelledEvent;
import com.pandurang.notifications.domain.models.OrderCreatedEvent;
import com.pandurang.notifications.domain.models.OrderDeliveredEvent;
import com.pandurang.notifications.domain.models.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Order Created Event: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Order Delivered Event: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Order Cancelled Event: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("Order Error Event: {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderErrorEvent with eventId: {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }
}
