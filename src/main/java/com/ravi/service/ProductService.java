package com.ravi.service;

import com.ravi.entities.Product;
import com.ravi.entities.Seller;
import com.ravi.requests.CreateProductRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest req, Seller seller);
    void deleteProduct(Long productId);
    Product updateProduct(Long productId, Product product);
    Product findProductById(Long productId);
    List<Product> searchProduct(String search);
    Page<Product> getAllProducts(
            String category,
            String brand,
            String colors,
            String sizes,
            Double minPrice,
            Double maxPrice,
            Double minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );


    List<Product> getProductBySellerId(Long sellerId);
}
