package com.inven.product_service.feign;


import com.inven.product_service.dto.StockDTO;
import com.inven.product_service.dto.StockRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stock-service", url = "${stock.service.url}")
public interface StockServiceClient {
    @PostMapping("/stocks")
    StockDTO createStock(@RequestBody StockRequestDTO stockDTO);

    @GetMapping("/stocks/product/{productId}")
    List<StockDTO> getStocksByProductId(@PathVariable Long productId);
}