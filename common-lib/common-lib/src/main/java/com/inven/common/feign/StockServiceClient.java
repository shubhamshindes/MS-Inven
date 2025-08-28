package com.inven.common.feign;



import com.inven.common.dto.StockDTO;
import com.inven.common.dto.StockRequestDTO;
import com.inven.common.feign.config.FeignConfig;
import com.inven.common.feign.fallback.StockServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stock-service",
//        url = "${stock.service.url}"),
        url = "${feign.client.config.stock-service.url}",
    configuration = FeignConfig.class,
fallback = StockServiceFallback.class)
public interface StockServiceClient {
    @PostMapping("/api/stocks")
    StockDTO createStock(@RequestBody StockRequestDTO stockDTO);

    @GetMapping("/api/stocks/product/{productId}")
    List<StockDTO> getStocksByProductId(@PathVariable Long productId);
}