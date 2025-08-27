package com.inven.stock_service.service;

import com.inven.stock_service.dto.StockDTO;
import com.inven.stock_service.dto.StockRequestDTO;

import java.util.List;

public interface StockService {
    StockDTO createStock(StockRequestDTO stockRequestDTO);
    StockDTO getStockById(Long id);
    List<StockDTO> getStocksByProductId(Long productId);
    StockDTO updateStock(Long id, StockRequestDTO stockRequestDTO);
    void deleteStock(Long id);
}