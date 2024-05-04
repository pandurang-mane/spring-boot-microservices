package com.pandurang.bookstore.webapp.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ProductController {

    @GetMapping
    String index() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    String productsPage() {
        return "products";
    }
}
