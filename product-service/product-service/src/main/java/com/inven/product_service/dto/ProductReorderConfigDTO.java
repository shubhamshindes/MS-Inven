package com.inven.product_service.dto;
import lombok.Data;

@Data
public class ProductReorderConfigDTO {
    private Long productId;
    private Integer reorderThreshold;
    private Integer reorderQuantity;
    private Boolean isAutoReorder;
}