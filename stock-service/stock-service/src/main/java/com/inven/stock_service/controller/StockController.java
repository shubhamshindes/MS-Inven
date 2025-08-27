package com.inven.stock_service.controller;
import com.inven.stock_service.dto.StockDTO;
import com.inven.stock_service.dto.StockRequestDTO;
import com.inven.stock_service.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping
    public ResponseEntity<StockDTO> createStock(@RequestBody StockRequestDTO stockRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.createStock(stockRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockDTO> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @GetMapping("/product/{productId}")
        public ResponseEntity<List<StockDTO>> getStocksByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStocksByProductId(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDTO> updateStock(@PathVariable Long id,
                                                @RequestBody StockRequestDTO stockRequestDTO) {
        return ResponseEntity.ok(stockService.updateStock(id, stockRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}