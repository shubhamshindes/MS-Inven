package com.inven.product_service.dto;

import lombok.Data;

@Data
public class StockRequestDTO {
    private Long productId;
    private Long shelfId;
    private Integer minThreshold;
    private Integer quantity;
}