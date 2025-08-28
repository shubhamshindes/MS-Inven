package com.inven.common.feign.fallback;

import com.inven.common.dto.StockDTO;
import com.inven.common.dto.StockRequestDTO;
import com.inven.common.exception.ResourceNotFoundException;
import com.inven.common.feign.StockServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class StockServiceFallback implements StockServiceClient {
    @Override
    public StockDTO createStock(StockRequestDTO stockDTO) {
        log.warn("Fallback triggered for createStock with productId: {}", stockDTO.getProductId());
        throw new ResourceNotFoundException("Stock service unavailable - cannot create stock");
    }
    @Override
    public List<StockDTO> getStocksByProductId(Long productId) {
        log.warn("Fallback triggered for getStocksByProductId with productId: {}", productId);
        return Collections.emptyList();
    }
}