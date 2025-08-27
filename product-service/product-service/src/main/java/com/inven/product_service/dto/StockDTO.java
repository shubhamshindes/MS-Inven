package com.inven.product_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockDTO {
    private Long stockId;
    private Long productId;
    private Long shelfId;
    private Integer minThreshold;
    private Integer quantity;
    private LocalDateTime lastUpdated;
}