package com.pandurang.orders.web.exception.controller;

import com.pandurang.orders.domain.OrderService;
import com.pandurang.orders.domain.SecurityService;
import com.pandurang.orders.domain.models.CreateOrderRequest;
import com.pandurang.orders.domain.models.CreatedOrderResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreatedOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String username = securityService.getLoginUserName();
        log.info("Creating order for user: "+username);
        return orderService.createOrder(username, request);
    }
}
