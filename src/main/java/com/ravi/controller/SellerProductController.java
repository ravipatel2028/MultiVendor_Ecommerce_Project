package com.ravi.controller;


import com.ravi.entities.Product;
import com.ravi.entities.Seller;
import com.ravi.exceptions.ProductException;
import com.ravi.exceptions.SellerException;
import com.ravi.requests.CreateProductRequest;
import com.ravi.service.ProductService;
import com.ravi.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers-products")
@RequiredArgsConstructor
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestHeader("Authorization") String jwt) throws ProductException, SellerException {

        Seller seller=sellerService.getSellerProfile(jwt);
        List<Product> products=productService.getProductBySellerId(seller.getId());
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest createProductRequest,
                                                 @RequestHeader("Authorization")  String jwt) throws ProductException, SellerException {
        Seller seller=sellerService.getSellerProfile(jwt);
        Product product=productService.createProduct(createProductRequest,seller);
        return ResponseEntity.ok(product);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } catch (ProductException productException) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId,
                             @Valid @RequestBody Product product) throws ProductException {
        Product existingProduct=productService.findProductById(productId);
        if(existingProduct==null){
            throw new ProductException("Product not found with given id : " + productId);
        }
        Product updatedProduct=productService.updateProduct(productId,product);
        return ResponseEntity.ok(updatedProduct);
    }
}
