package com.inven.product_service.service;

import com.inven.product_service.dto.ProductDTO;
import com.inven.product_service.dto.ProductRequestDTO;
import com.inven.product_service.dto.ProductReorderConfigDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long id);
    ProductDTO updateReorderConfig(ProductReorderConfigDTO config);
    List<ProductDTO> getProductsNeedingReorder();

}