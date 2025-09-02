package com.imperialnet.customer_service.application.dto;

import lombok.*;

/**
 * DTO para devolver información de un cliente (por ejemplo, en GET).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String status;
}
