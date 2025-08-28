package com.inven.common.feign.fallback;

import com.inven.common.dto.SupplierDTO;
import com.inven.common.exception.ResourceNotFoundException;
import com.inven.common.feign.SupplierServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SupplierServiceFallback implements SupplierServiceClient {

    @Override
    public SupplierDTO getSupplierById(Long id) {
        log.warn("Fallback triggered for getSupplierById with id: {}", id);
        // Return a dummy supplier or throw exception based on your requirement
        throw new ResourceNotFoundException("Supplier service unavailable - fallback triggered");
    }
}