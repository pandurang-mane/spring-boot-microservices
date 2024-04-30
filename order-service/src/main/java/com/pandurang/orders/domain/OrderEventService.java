package com.pandurang.orders.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pandurang.orders.domain.models.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventService {

    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;

    OrderEventService(
            OrderEventRepository orderEventRepository,
            ObjectMapper objectMapper,
            OrderEventPublisher orderEventPublisher) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = objectMapper;
        this.orderEventPublisher = orderEventPublisher;
    }

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(OrderEventType.ORDER_CREATED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setPayload(toJsonPayload(event));
        orderEventEntity.setCreatedAt(event.createdAt());
        this.orderEventRepository.save(orderEventEntity);
    }

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setPayload(toJsonPayload(event));
        orderEventEntity.setCreatedAt(event.createdAt());
        this.orderEventRepository.save(orderEventEntity);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setPayload(toJsonPayload(event));
        orderEventEntity.setCreatedAt(event.createdAt());
        this.orderEventRepository.save(orderEventEntity);
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEventEntity = new OrderEventEntity();
        orderEventEntity.setEventId(event.eventId());
        orderEventEntity.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEventEntity.setOrderNumber(event.orderNumber());
        orderEventEntity.setPayload(toJsonPayload(event));
        orderEventEntity.setCreatedAt(event.createdAt());
        this.orderEventRepository.save(orderEventEntity);
    }

    private String toJsonPayload(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        log.info("Found {} Order Events to be published", events.size());
        for (OrderEventEntity event : events) {
            this.publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType) {
            case ORDER_CREATED -> {
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
            }
            case ORDER_DELIVERED -> {
                OrderDeliveredEvent orderDeliveredEvent =
                        fromJsonPayload(event.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderDeliveredEvent);
            }
            case ORDER_CANCELLED -> {
                OrderCancelledEvent orderCancelledEvent =
                        fromJsonPayload(event.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderCancelledEvent);
            }
            case ORDER_PROCESSING_FAILED -> {
                OrderErrorEvent orderErrorEvent = fromJsonPayload(event.getPayload(), OrderErrorEvent.class);
                orderEventPublisher.publish(orderErrorEvent);
            }
            default -> log.warn("Unsupported Event Type: {}", event.getEventType());
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
