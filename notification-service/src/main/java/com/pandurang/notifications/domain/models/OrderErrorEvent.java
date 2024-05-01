package com.pandurang.notifications.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address address,
        String reason,
        LocalDateTime createdAt) {}
