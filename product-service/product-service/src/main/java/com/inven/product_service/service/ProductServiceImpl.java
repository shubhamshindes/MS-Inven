package com.inven.product_service.service;

import com.inven.product_service.dto.*;
import com.inven.product_service.exception.ResourceNotFoundException;
import com.inven.product_service.feign.StockServiceClient;
import com.inven.product_service.feign.SupplierServiceClient;
import com.inven.product_service.model.Product;
import com.inven.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SupplierServiceClient supplierServiceClient;
    private final StockServiceClient stockServiceClient;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO productRequestDTO) {
        // Verify supplier exists
        SupplierDTO supplier = supplierServiceClient.getSupplierById(productRequestDTO.getSupplierId());

        // Create product
        Product product = modelMapper.map(productRequestDTO, Product.class);
        Product savedProduct = productRepository.save(product);

        // Create initial stock
        StockRequestDTO stockRequestDTO = new StockRequestDTO();
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
        List<StockDTO> stocks = stockServiceClient.getStocksByProductId(id);
        productDTO.setStocks(stocks);

        // Fetch supplier details
        SupplierDTO supplier = supplierServiceClient.getSupplierById(product.getSupplierId());
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

    @Override
    public List<ProductDTO> getProductsNeedingReorder() {
        // Get all products with auto-reorder enabled
        List<Product> autoReorderProducts = productRepository.findByIsAutoReorderTrue();

        return autoReorderProducts.stream()
                .map(this::convertToDTO)
                .filter(productDTO -> {
                    try {
                        // For each product, check stock levels using Feign client
                        List<StockDTO> stocks = stockServiceClient.getStocksByProductId(productDTO.getProductId());

                        // Check if any stock location is below threshold
                        return stocks.stream()
                                .anyMatch(stock -> stock.getQuantity() <= productDTO.getReorderThreshold());
                    } catch (Exception e) {
                        // Handle case where stock service is unavailable
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
}