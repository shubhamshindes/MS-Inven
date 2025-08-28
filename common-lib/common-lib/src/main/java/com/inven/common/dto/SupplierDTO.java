package com.inven.common.dto;

import lombok.Data;

@Data
public class SupplierDTO {
    private Long supplierId;
    private String supplierName;
    private String contactNumber;
    private String email;
    private String address;
}