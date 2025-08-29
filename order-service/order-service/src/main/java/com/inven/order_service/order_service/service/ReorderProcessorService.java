package com.inven.order_service.order_service.service;

import com.inven.common.dto.ReorderEvent;
import com.inven.common.dto.SupplierDTO;
import com.inven.common.feign.SupplierServiceClient;
import com.inven.order_service.order_service.model.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReorderProcessorService {

   private final KafkaTemplate<String, ReorderEvent> kafkaTemplate;

    @KafkaListener(topics = "product-reorder-events", groupId = "order-service-group")
    public void processReorderEvent(ReorderEvent event) {
        log.info("Processing reorder event: {}", event.getEventId());

        try {
            // Get supplier details
            SupplierServiceClient supplierServiceClient;
            SupplierDTO supplier = supplierServiceClient.getSupplierById(event.getSupplierId());

            // Create purchase order
            PurchaseOrder order = createPurchaseOrder(event, supplier);
            orderRepository.save(order);

            // Update event status
            event.setStatus("PROCESSED");
            kafkaTemplate.send("reorder-events-status", event.getEventId(), event);

            log.info("Successfully processed reorder for product: {}", event.getProductName());

        } catch (Exception e) {
            log.error("Failed to process reorder event: {}", event.getEventId(), e);
            event.setStatus("FAILED");
            kafkaTemplate.send("reorder-events-status", event.getEventId(), event);
        }
    }

    private PurchaseOrder createPurchaseOrder(ReorderEvent event, SupplierDTO supplier) {
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNumber(generateOrderNumber());
        order.setSupplierId(event.getSupplierId());
        order.setProductId(event.getProductId());
        order.setQuantity(event.getReorderQuantity());
        order.setStatus("PENDING");
        order.setCreatedDate(LocalDateTime.now());
        return order;
    }

    private String generateOrderNumber() {
        return "PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
