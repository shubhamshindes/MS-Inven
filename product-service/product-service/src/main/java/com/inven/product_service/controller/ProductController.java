package com.inven.product_service.controller;
import com.inven.common.dto.ProductDTO;
import com.inven.product_service.dto.ProductRequestDTO;
import com.inven.product_service.dto.ProductReorderConfigDTO;
import com.inven.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reorder-config")
    public ResponseEntity<ProductDTO> updateReorderConfig(@RequestBody ProductReorderConfigDTO config) {
        return ResponseEntity.ok(productService.updateReorderConfig(config));
    }

    @GetMapping("/needing-reorder")
    public ResponseEntity<List<ProductDTO>> getProductsNeedingReorder() {
        return ResponseEntity.ok(productService.getProductsNeedingReorder());
    }
}
