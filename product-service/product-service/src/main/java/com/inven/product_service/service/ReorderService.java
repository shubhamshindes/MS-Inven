package com.inven.product_service.service;

import com.inven.common.dto.ReorderEvent;
import com.inven.common.feign.StockServiceClient;
import com.inven.common.feign.SupplierServiceClient;
import com.inven.product_service.model.Product;
import com.inven.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReorderService {

    private final KafkaTemplate<String, ReorderEvent> kafkaTemplate;
    private final ProductRepository productRepository;
    private final StockServiceClient stockServiceClient; // Add this
    private final SupplierServiceClient supplierServiceClient; // Add this
    private final ModelMapper modelMapper;

    private static final String REORDER_TOPIC = "product-reorder-events";

    @Scheduled(fixedRate = 300000) // Check every 5 minutes
    public void checkAndTriggerAutoReorder() {
        log.info("Starting auto-reorder check...");

        List<Product> productsNeedingReorder = productRepository.findProductsNeedingReorder();

        productsNeedingReorder.forEach(product -> {
            try {
                ReorderEvent event = createReorderEvent(product);
                kafkaTemplate.send(REORDER_TOPIC, event.getProductId().toString(), event);
                log.info("Sent reorder event for product: {}", product.getProductName());
            } catch (Exception e) {
                log.error("Failed to send reorder event for product: {}", product.getProductId(), e);
            }
        });
    }

    private ReorderEvent createReorderEvent(Product product) {
        return ReorderEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .reorderQuantity(product.getReorderQuantity())
                .supplierId(product.getSupplierId())
                .eventTimestamp(LocalDateTime.now())
                .status("PENDING")
                .build();
    }
}