package com.pandurang.bookstore.catalog.domain;

public class ProductNotFountException extends RuntimeException {
    ProductNotFountException(String message) {
        super(message);
    }

    public static ProductNotFountException forCode(String code) {
        return new ProductNotFountException("Product with code " + code + " not found!");
    }
}
