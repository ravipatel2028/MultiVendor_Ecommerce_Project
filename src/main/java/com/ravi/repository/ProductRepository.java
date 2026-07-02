package com.ravi.repository;

import com.ravi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
    List<Product> findBySellerId(Long sellerId);

    @Query("SELECT p FROM Product p " +
            "WHERE lower(p.title) LIKE lower(concat('%', COALESCE(:query, ''), '%'))" +
            "   OR lower(p.category.name) LIKE lower(concat('%', COALESCE(:query, ''), '%'))"
    )
    List<Product> searchProduct(@Param("query")String query);
}
