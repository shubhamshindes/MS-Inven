package com.inven.common.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private String productCode;
    private Double price;
    private String productCategory;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private String productDescription;
    private String imageUrl;
    private Long supplierId;
    private SupplierDTO supplier;
    private Integer reorderThreshold;
    private Integer reorderQuantity;
    private Boolean isAutoReorder;
    private List<StockDTO> stocks;
}