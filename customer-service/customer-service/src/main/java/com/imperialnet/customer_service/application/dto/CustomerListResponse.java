package com.imperialnet.customer_service.application.dto;

import lombok.*;

import java.util.List;

/**
 * DTO para devolver una lista de clientes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerListResponse {

    private List<CustomerResponse> customers;
}
