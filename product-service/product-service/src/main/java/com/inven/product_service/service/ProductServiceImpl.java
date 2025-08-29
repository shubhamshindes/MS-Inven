package com.inven.product_service.service;

import com.inven.common.dto.ProductDTO;
import com.inven.common.dto.StockDTO;
import com.inven.common.feign.StockServiceClient;
import com.inven.common.feign.SupplierServiceClient;
import com.inven.product_service.dto.*;
import com.inven.product_service.exception.ResourceNotFoundException;
import com.inven.product_service.model.Product;
import com.inven.product_service.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final StockServiceClient stockServiceClient; // Add this
    private final SupplierServiceClient supplierServiceClient; // Add this


    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO productRequestDTO) {
        // Verify supplier exists
        com.inven.common.dto.SupplierDTO supplier = supplierServiceClient.getSupplierById(productRequestDTO.getSupplierId());

        // Create product
        Product product = modelMapper.map(productRequestDTO, Product.class);
        Product savedProduct = productRepository.save(product);

        // Create initial stock
        com.inven.common.dto.StockRequestDTO stockRequestDTO = new com.inven.common.dto.StockRequestDTO();
        stockRequestDTO.setProductId(savedProduct.getProductId());
        stockRequestDTO.setShelfId(productRequestDTO.getShelfId());
        stockRequestDTO.setQuantity(productRequestDTO.getInitialQuantity());
        stockRequestDTO.setMinThreshold(productRequestDTO.getReorderThreshold() != null ?
                productRequestDTO.getReorderThreshold() : 10);

        stockServiceClient.createStock(stockRequestDTO);

        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductDTO productDTO = convertToDTO(product);

        // Fetch stocks for this product
        List<com.inven.common.dto.StockDTO> stocks = stockServiceClient.getStocksByProductId(id);
        productDTO.setStocks(stocks);

        // Fetch supplier details
        com.inven.common.dto.SupplierDTO supplier = supplierServiceClient.getSupplierById(product.getSupplierId());
        productDTO.setSupplier(supplier);

        return productDTO;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        modelMapper.map(productRequestDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);

        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }
//to update particular stocks threshold and reOrder quantity in database
    @Override
    public ProductDTO updateReorderConfig(ProductReorderConfigDTO config) {
        Product product = productRepository.findById(config.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setReorderThreshold(config.getReorderThreshold());
        product.setReorderQuantity(config.getReorderQuantity());
        product.setIsAutoReorder(config.getIsAutoReorder());

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @CircuitBreaker(name = "stockService", fallbackMethod = "getProductsNeedingReorderFallback")
    @TimeLimiter(name = "stockService")
    @Retry(name = "stockService")
    @Override
    public List<ProductDTO> getProductsNeedingReorder() {
        List<Product> autoReorderProducts = productRepository.findByIsAutoReorderTrue();

        return autoReorderProducts.stream()
                .map(this::convertToDTO)
                .filter(productDTO -> {
                    List<StockDTO> stocks = stockServiceClient.getStocksByProductId(productDTO.getProductId());
                    return stocks.stream()
                            .anyMatch(stock -> stock.getQuantity() <= productDTO.getReorderThreshold());
                })
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsNeedingReorderFallback(Exception e) {
        log.warn("Fallback triggered for getProductsNeedingReorder due to: {}", e.getMessage());
        // Return empty list or cached data as fallback
        return Collections.emptyList();
    }

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}
