package com.pandurang.orders.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address address,
        String reason,
        LocalDateTime createdAt) {}
