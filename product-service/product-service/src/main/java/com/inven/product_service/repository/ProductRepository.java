package com.inven.product_service.repository;


import com.inven.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

     @Query(value = "SELECT p.* FROM products p " +
            "WHERE p.is_auto_reorder = true " +
            "AND EXISTS (SELECT 1 FROM stocks s WHERE s.product_id = p.product_id AND s.quantity <= p.reorder_threshold)",
            nativeQuery = true)
    List<Product> findProductsNeedingReorder();

    boolean existsByProductCode(String productCode);

    List<Product> findByIsAutoReorderTrue();
}