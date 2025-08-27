package com.inven.supplier_service.service;
import com.inven.supplier_service.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {
    SupplierDTO createSupplier(SupplierDTO supplierDTO);
    SupplierDTO getSupplierById(Long id);
    List<SupplierDTO> getAllSuppliers();
    SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO);
    void deleteSupplier(Long id);
}