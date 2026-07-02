package com.ravi.service;

import com.ravi.entities.Product;
import com.ravi.entities.Seller;
import com.ravi.exceptions.ProductException;
import com.ravi.requests.CreateProductRequest;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest req, Seller seller);
    void deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProduct(String query) throws ProductException;
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
