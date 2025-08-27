package com.inven.product_service.feign;

import com.inven.product_service.dto.SupplierDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "supplier-service", url = "${supplier.service.url}")
public interface SupplierServiceClient {
    @GetMapping("/api/suppliers/{id}")
    SupplierDTO getSupplierById(@PathVariable Long id);
}