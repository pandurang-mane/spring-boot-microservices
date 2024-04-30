package com.pandurang.orders.domain;

import com.pandurang.orders.clients.catalog.Product;
import com.pandurang.orders.clients.catalog.ProductServiceClient;
import com.pandurang.orders.domain.models.CreateOrderRequest;
import com.pandurang.orders.domain.models.OrderItem;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class OrderValidator {

    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient client;

    OrderValidator(ProductServiceClient client) {
        this.client = client;
    }

    void validate(CreateOrderRequest request) {
        Set<OrderItem> items = request.items();
        for (OrderItem item : items) {
            Product product = client.getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid product code: " + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                log.error(
                        "Product Price not matching. Actual price: {}, Received Price: {}",
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
