package com.inven.stock_service.repository;

import com.inven.stock_service.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByProductId(Long productId);
    boolean existsByProductIdAndShelfId(Long productId, Long shelfId);
}
