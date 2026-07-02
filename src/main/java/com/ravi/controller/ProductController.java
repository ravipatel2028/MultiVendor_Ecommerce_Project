package com.ravi.controller;


import com.ravi.entities.Product;
import com.ravi.exceptions.ProductException;
import com.ravi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) throws ProductException {

            Product product=productService.findProductById(productId);
            return ResponseEntity.ok().body(product);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String query) throws ProductException {
            List<Product> products=productService.searchProduct(query);
            return ResponseEntity.ok().body(products);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam(required = false) String category,
                                                        @RequestParam(required = false) String brand,
                                                        @RequestParam(required = false) String color,
                                                        @RequestParam(required = false) String size,
                                                        @RequestParam(required = false) Double minPrice,
                                                        @RequestParam(required = false) Double maxPrice,
                                                        @RequestParam(required = false) Double minDiscount,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(required = false) String stock,
                                                        @RequestParam(defaultValue= "0") Integer pageNumber
                                                        ) throws ProductException {

        return ResponseEntity.ok(productService
                .getAllProducts(category, brand, color, size,
                        minPrice, maxPrice, minDiscount, sort, stock, pageNumber ));
    }

}
