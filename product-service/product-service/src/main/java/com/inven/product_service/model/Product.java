package com.inven.product_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "product_category", nullable = false)
    private String productCategory;

    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "product_description", length = 500)
    private String productDescription;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId; // Reference to supplier service

    @Column(name = "reorder_threshold")
    private Integer reorderThreshold = 10;

    @Column(name = "reorder_quantity")
    private Integer reorderQuantity = 50;

    @Column(name = "is_auto_reorder")
    private Boolean isAutoReorder = true;
}
