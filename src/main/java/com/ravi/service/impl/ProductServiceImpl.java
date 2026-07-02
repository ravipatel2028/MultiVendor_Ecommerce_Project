package com.ravi.service.impl;

import com.ravi.entities.Category;
import com.ravi.entities.Product;
import com.ravi.entities.Seller;
import com.ravi.exceptions.ProductException;
import com.ravi.repository.CategoryRepository;
import com.ravi.repository.ProductRepository;
import com.ravi.requests.CreateProductRequest;
import com.ravi.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product createProduct(CreateProductRequest req, Seller seller) {
        Category category1 = categoryRepository.findByCategoryId(req.getCategory());

        if(category1 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(1);
            category1=categoryRepository.save(category);
        }

        Category category2 = categoryRepository.findByCategoryId(req.getCategory());
        if(category2 == null){
            Category category = new Category();
            category.setCategoryId(req.getCategory());
            category.setLevel(2);
            category2=categoryRepository.save(category);
        }

        Category category3 = categoryRepository.findByCategoryId(req.getCategory());
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
            throw new IllegalArgumentException("MrpPrice must be greater than sellingPrice");
        }
        else if(sellingPrice < 0){
            throw new IllegalArgumentException("SellingPrice must be greater than 0");
        }
        return ((mrpPrice - sellingPrice)/mrpPrice)*100;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductException("Product with given Id : " +productId + " not found."));
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        Product oldProduct = productRepository.findById(productId)
                .orElseThrow(()->new ProductException("Product with Id : "+productId+" not found."));
        oldProduct.setId(product.getId());
        return productRepository.save(oldProduct);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId)
                .orElseThrow(()->new ProductException("Product with given product id" + productId+" not found."));
    }

    @Override
    public List<Product> searchProduct(String query) {

        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors, String sizes, Double minPrice, Double maxPrice, Double minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> specification =(root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(category != null){
                Join<Product,  Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if(colors !=null && !colors.isEmpty() ){
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }
            if(sizes !=null && !sizes.isEmpty() ){
                predicates.add(criteriaBuilder.equal(root.get("size"), sizes));
            }
            if(minPrice != null ){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if(maxPrice != null ){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if(minDiscount != null ){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), minDiscount));
            }
            if(stock !=null && !stock.isEmpty() ){
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;

        if(sort!=null && !sort.isEmpty()){
            pageable = switch (sort) {
                case "price_low" -> PageRequest.of(pageNumber != null ?
                        pageNumber : 0, 10, Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber != null ?
                        pageNumber : 0, 10, Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber != null ?
                        pageNumber : 0, 10, Sort.unsorted());
            };
        }else{
            pageable= PageRequest.of(pageNumber != null ? pageNumber : 0, 10, Sort.unsorted());
        }
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
