package com.inven.product_service.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductRequestDTO {
    private String productName;
    private String productCode;
    private Double price;
    private String productCategory;
    private LocalDate manufactureDate;
    private LocalDate expiryDate;
    private String productDescription;
    private String imageUrl;
    private Long supplierId;
    private Long shelfId;
    private Integer initialQuantity;
    private Integer reorderThreshold;
    private Integer reorderQuantity;
    private Boolean isAutoReorder;
}