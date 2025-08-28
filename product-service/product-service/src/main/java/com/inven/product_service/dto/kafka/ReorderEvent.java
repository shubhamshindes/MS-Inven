package com.inven.product_service.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReorderEvent {
    private String eventId;
    private Long productId;
    private String productName;
    private String productCode;
    private Integer reorderQuantity;
    private Long supplierId;
    private LocalDateTime eventTimestamp;
    private String status; // PENDING, PROCESSED, FAILED
}