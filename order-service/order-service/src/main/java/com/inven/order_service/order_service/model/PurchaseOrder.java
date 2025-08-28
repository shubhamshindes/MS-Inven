package com.inven.order_service.order_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private Long supplierId;
    private String supplierName;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String status;
    private LocalDateTime createdDate;
}