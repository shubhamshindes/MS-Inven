package com.inven.supplier_service.service;
import com.inven.supplier_service.dto.SupplierDTO;
import com.inven.supplier_service.exception.ResourceNotFoundException;
import com.inven.supplier_service.model.Supplier;
import com.inven.supplier_service.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Override
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDTO(savedSupplier);
    }

    @Override
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return convertToDTO(supplier);
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        modelMapper.map(supplierDTO, existingSupplier);
        Supplier updatedSupplier = supplierRepository.save(existingSupplier);

        return convertToDTO(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplierRepository.delete(supplier);
    }

    private SupplierDTO convertToDTO(Supplier supplier) {
        return modelMapper.map(supplier, SupplierDTO.class);
    }
}