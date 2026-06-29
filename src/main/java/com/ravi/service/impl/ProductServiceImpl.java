package com.ravi.service.impl;

import com.ravi.entities.Category;
import com.ravi.entities.Product;
import com.ravi.entities.Seller;
import com.ravi.repository.CategoryRepository;
import com.ravi.repository.ProductRepository;
import com.ravi.requests.CreateProductRequest;
import com.ravi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategotryId(req.getCategory());

        if(category1 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category1=categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategotryId(req.getCategory());
        if(category2 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(2);
            category2=categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategotryId(req.getCategory());
        if(category3 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(3);
            category3=categoryRepository.save(category);
        }

        Double discountPercentage=calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product  product = new Product();

        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setColor(req.getColor());
        product.setTitle(req.getTitle());
        product.setSellingPrice(req.getSellingPrice());
        product.setDiscountPercentage(discountPercentage);
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSize());

        return productRepository.save(product);
    }

    private Double calculateDiscountPercentage(Double mrpPrice, Double sellingPrice) {
        if(mrpPrice < sellingPrice){

        }
        return ((mrpPrice - sellingPrice)/mrpPrice)*100;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        return null;
    }

    @Override
    public Product findProductById(Long productId) {
        return null;
    }

    @Override
    public List<Product> searchProduct(String search) {
        return List.of();
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Double minPrice, Double maxPrice, Double minDiscount, String sort, String stock, Integer pageNumber) {
        return null;
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return List.of();
    }
}
