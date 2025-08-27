package com.inven.stock_service.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long stockId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "shelf_id", nullable = false)
    private Long shelfId;

    @Column(name = "min_threshold")
    private Integer minThreshold = 10;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public void addQuantity(Integer qty) {
        this.quantity += qty;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean needsReorder() {
        return quantity <= minThreshold;
    }
}