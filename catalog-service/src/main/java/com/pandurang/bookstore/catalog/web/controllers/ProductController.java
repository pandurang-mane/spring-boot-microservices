package com.pandurang.bookstore.catalog.web.controllers;

import com.pandurang.bookstore.catalog.domain.PagedResult;
import com.pandurang.bookstore.catalog.domain.Product;
import com.pandurang.bookstore.catalog.domain.ProductNotFountException;
import com.pandurang.bookstore.catalog.domain.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    PagedResult<Product> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<Product> getProductByCode(@PathVariable String code) {
        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFountException.forCode(code));
    }
}
